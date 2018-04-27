/*
 * file: log.c
 * function: log mudule
 * data: 2014-08-03
 * others:
 */

#include <assert.h>
#include <stdarg.h>
#include "log.h"

#ifdef PLATFORM_ANDROID
#include <android/log.h>
#endif

void (*on_log_listener)(char* format,char* message);

static int debug_flag = 1;
void log_set_debug(int is_debug)
{
	if(is_debug == 0){
		debug_flag = 0;
	}else{
		debug_flag = 1;
	}
	return;
}

/*******************************************************************************
function: open  log module
input: category   ----data address

ouput: none
return: -1: fail, 0: success
 */
int log_open(const char *category)
{
	if(category == NULL)
		category = NULL;
	return 0;
}

/************************************************************************
function: output logs
input: priority   ---log level
 file       ---file name where logs come from  
 line       ----line of the file where logs come from
 fmt        ----format
 output: none
return: none
 ***********************************************************************/
void log_message(int priority, const char *file, int line, const char *fmt, ...)
{
	if(debug_flag == 0){
		return;
	}
	char new_fmt[1024]={0};
	const char *head_fmt = "[file:%s,line:%d]:";
	va_list ap;
	int n;
	n = sprintf(new_fmt, head_fmt, file, line);
	if(n == 0 && priority == 0){
		n = 0;
	}

	char buf[1024*10]={0};
	va_start(ap, fmt);
	vsnprintf(buf,1024*10,fmt, ap);
#ifdef PLATFORM_ANDROID
	int pri = ANDROID_LOG_DEBUG;
	switch (priority){
		case LOG_PRI_ERROR:
			pri=ANDROID_LOG_ERROR;
			break;
		case LOG_PRI_WARN:
			pri=ANDROID_LOG_WARN;
			break;
		case LOG_PRI_NOTICE:
			pri=ANDROID_LOG_INFO;
			break;
		case LOG_PRI_DEBUG:
			pri=ANDROID_LOG_DEBUG;
			break;
		case LOG_PRI_TRACE:
			pri=ANDROID_LOG_VERBOSE;
			break;
		default:
			break;
	}
	__android_log_print(pri,"smit","%s%s",new_fmt,buf);
#else
	fprintf(stdout,"%s",new_fmt);
	fprintf(stdout,"%s",buf);
	fflush(stdout);
#endif
	if(on_log_listener){
		on_log_listener(new_fmt,buf);
	}

    va_end(ap);
}
/*******************************************************************************
fuonction: close log moudule
input: none
output: none
return: 0: success -1: fail
 */
int log_close(void)
{
	return 0;
}
/*******************************************************************************
 function: set listener event
 input: set callback
 output: none
 return: none
 */
void log_set_listener(void (*listener)(char* format,char* message))
{
	on_log_listener = listener;
}
