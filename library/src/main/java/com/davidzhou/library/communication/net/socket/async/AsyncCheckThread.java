package com.davidzhou.library.communication.net.socket.async;

import android.os.Handler;

import com.davidzhou.library.communication.common.constants.CommErrorConsts;
import com.davidzhou.library.communication.common.dto.BaseDataInfo;
import com.davidzhou.library.communication.common.dto.ErrorMsg;
import com.davidzhou.library.communication.handler.CommMainMsgHandler;
import com.davidzhou.library.communication.interfaces.CommEventCallback;
import com.davidzhou.library.communication.net.socket.BaseSocket;
import com.davidzhou.library.util.LogUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Created by David on 2017/12/27 0027.
 */

public class AsyncCheckThread extends Thread {
    private static final String TAG = "AsyncCheckThread";
    private static final int INTERVAL = 200;
    private static final int SELECT_TIMEOUT = 500;
    private static final int SOCKET_TIMEOUT = 10000;
    private static final int CLOSE_TIMEOUT = 5000;

    private Handler mHandler;
    private CommMainMsgHandler msgHandler;

    private enum Task {
        CONNECT,
        LISTENING,
        UNKNOWN
    }

    private Task mTask;
    private Selector mSelector;
    private SocketChannel mSocketChannel;
    private boolean isDoing = true;
    private byte[] sendData;
    private String mAddr;
    private int mPort;


    public AsyncCheckThread(CommMainMsgHandler handler) {
        mTask = Task.CONNECT;
        mHandler = new Handler();
        this.msgHandler = handler;
    }

    public void send(byte[] data) {
        this.sendData = data;
    }

    public void exit() {
        isDoing = false;
    }

    private void handlerWriteChannel() {
        if (sendData != null) {
            if (!mSocketChannel.isConnected()) {
                LogUtil.e(TAG, "not connected before write");
                return;
            }
            try {
                ByteBuffer byteBuffer = ByteBuffer.wrap(sendData);
                int sendCount = mSocketChannel.write(byteBuffer);
                {
                    if (sendCount == sendData.length) {
                        sendData = null;
                    } else {
                        LogUtil.e(TAG, "sendCount is not equal the data's length");
                    }
                    // 未一次发送成功的情况暂时不考虑，需要增加策略来实现
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handlerReadChannel() {
        try {
            if (!mSocketChannel.isConnected()) {
                LogUtil.e(TAG, "not connected before read");
                return;
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(BaseSocket.RECV_BUFF_CAPACITY);
            int recvCount = mSocketChannel.read(byteBuffer);
            if (recvCount != -1) {
                byte[] data = new byte[recvCount];
                System.arraycopy(byteBuffer.array(), 0, data, 0, recvCount);
                BaseDataInfo info = new BaseDataInfo(data);
                msgHandler.sendReadMsg(info);
            } else {
                // receive -1 means the connection has broken
                mSocketChannel.close();
                reset();
                msgHandler.sendDisconnectMsg();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return mSocketChannel != null && mSocketChannel.isConnected();
    }

    private void handlerConnectChannel() {
        try {
            if (mSocketChannel.isConnectionPending()) {
                mHandler.removeCallbacks(closeRunnable);
                LogUtil.i(TAG, "connecting");
                if (mSocketChannel.finishConnect()) {
                    LogUtil.i(TAG, "connected");
                    mTask = Task.LISTENING;
                    msgHandler.sendConnectMsg();
                } else {
                    ErrorMsg errorMsg = new ErrorMsg();
                    errorMsg.setErrorCode(CommErrorConsts.ERROR_CODE_CONNECT_FAILED);
                    msgHandler.sendErrorMsg(errorMsg);
                }
            } else {
                LogUtil.e(TAG, "no connecting");
                mHandler.postDelayed(closeRunnable, CLOSE_TIMEOUT);
            }
        } catch (IOException e) {
            e.printStackTrace();
            ErrorMsg errorMsg = new ErrorMsg();
            errorMsg.setErrorCode(CommErrorConsts.ERROR_CODE_CONNECT_FAILED);
            msgHandler.sendErrorMsg(errorMsg);
        }
    }
    private Runnable closeRunnable = new Runnable() {
        @Override
        public void run() {
            disconnect();
        }
    };

    public void disconnect() {
        if (mSocketChannel != null) {
            try {
                mSocketChannel.close();
                reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void connect() throws IOException {
        boolean rst;
        if (mSelector == null) {
            isDoing = true;
            mTask = Task.CONNECT;
            SocketAddress socketAddress = new InetSocketAddress(mAddr, mPort);
//            mSocketChannel = SocketChannel.open(socketAddress);
            mSocketChannel = SocketChannel.open();
            mSocketChannel.socket().setSoTimeout(SOCKET_TIMEOUT);
            mSocketChannel.configureBlocking(false);
            mSelector = Selector.open();
            rst = mSocketChannel.connect(socketAddress);
        }
        rst = mSocketChannel.isConnectionPending();
        rst = mSocketChannel.finishConnect();
//        LogUtil.i(TAG, "after finishConnect, rst = " + rst);
        SelectionKey sk = mSocketChannel.register(mSelector,
                SelectionKey.OP_CONNECT);
        int num = mSelector.select(SELECT_TIMEOUT);
        if (num > 0) {
//            LogUtil.i(TAG, "selectors: " + num);
            for (SelectionKey selectionKey : mSelector.selectedKeys()) {
                if (selectionKey.isConnectable()) {
//                    LogUtil.d(TAG, "selectionKey isConnectable");
                    handlerConnectChannel();
                }
                mSelector.selectedKeys().remove(selectionKey);
            }
        }
    }

    private void listen() throws IOException {
        SelectionKey sk = mSocketChannel.register(mSelector,
                SelectionKey.OP_WRITE
                        | SelectionKey.OP_READ);
        int num = mSelector.select(SELECT_TIMEOUT);
        if (num > 0) {
//            LogUtil.i(TAG, "selectors: " + num);
            for (SelectionKey selectionKey : mSelector.selectedKeys()) {
                if (selectionKey.isWritable()) {
//                    LogUtil.i(TAG, "selectionKey isWritable");
                    handlerWriteChannel();
                }
                if (selectionKey.isReadable()) {
//                    LogUtil.i(TAG, "selectionKey isReadable");
                    handlerReadChannel();
                }
                // 最后删掉当前的键值
                mSelector.selectedKeys().remove(selectionKey);
            }
        }
    }

    public void setAddrAndPort(String addr, int port) {
        this.mAddr = addr;
        this.mPort = port;
    }

    @Override
    public void run() {
        super.run();
//        LogUtil.i(TAG, "run");
        try {
            do {
                switch (mTask) {
                    case CONNECT:
//                        LogUtil.i(TAG, "task connect");
                        connect();
                        break;
                    case LISTENING:
//                        LogUtil.i(TAG, "task listening");
                        listen();
                        break;
                    case UNKNOWN:
                        break;
                }
                Thread.sleep(INTERVAL);
            } while (isDoing);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reset() {
        mSocketChannel = null;
        mSelector = null;
        isDoing = false;
        mTask = Task.UNKNOWN;
    }
}
