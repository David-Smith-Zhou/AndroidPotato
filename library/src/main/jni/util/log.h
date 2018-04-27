#ifndef _LOG_H_
#define _LOG_H_

#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#define PLATFORM_ANDROID

/* Log level*/
#define LOG_PRI_ERROR 		1
#define LOG_PRI_WARN 		2
#define LOG_PRI_NOTICE 		3
#define LOG_PRI_DEBUG 		4
#define LOG_PRI_TRACE 		5

int log_open(const char *category);
void log_message(int priority, const char *file, int line, const char *fmt, ...);
void log_trace(const char *file, int line, const char *fmt, ...);
int log_close();
void log_set_debug(int is_debug);
void log_set_listener(void (*listener)(char* format,char* message));

#define LOG_ERROR(fmt , args...)	\
	log_message(LOG_PRI_ERROR, __FILE__, __LINE__, fmt ,## args)
#define LOG_WARN(fmt, args...)		\
	log_message(LOG_PRI_WARN, __FILE__, __LINE__, fmt ,## args)
#define LOG_NOTICE(fmt , args...)	\
	log_message(LOG_PRI_NOTICE, __FILE__, __LINE__, fmt ,## args)
#define LOG_DEBUG(fmt , args...)	\
	log_message(LOG_PRI_DEBUG, __FILE__, __LINE__, fmt ,## args)
#define LOG_TRACE(fmt,args...) 		\
	log_message(LOG_PRI_TRACE, __FILE__, __LINE__, fmt ,## args)

/* Print debug information*/
#define ERRBUFLEN 512
#define DEBUG_ON
#ifdef DEBUG_ON
#define print_error(str) \
	do \
	{ \
		char errbuf[ERRBUFLEN] = {'\0'}; \
		snprintf(errbuf, ERRBUFLEN, "[file: %s line: %d] %s", \
									__FILE__, __LINE__, str); \
		fprintf(stderr, "\033[31m"); \
		perror(errbuf); \
		fprintf(stderr, "\033[0m"); \
	} while (0)
#define print_info(str) \
	do \
	{ \
		printf("\033[31m"); \
		printf("[file: %s line: %d] %s\n", __FILE__, __LINE__, str); \
		printf("\033[0m"); \
		fprintf(stderr, "\033[0m"); \
	} while(0)
#else
#define  print_error(str)
#define  print_info(str)
#endif

#endif
