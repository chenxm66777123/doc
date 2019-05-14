#!/bin/sh  
#工作空间地址 
RUN_DIR=/home/application/spring-server
#项目运行放一些临时文件  
CACHE_DIR=/home/application/cache
#配置环境变量 $PATH读取之前的环境变量用：进行连接  
PATH=$PATH:$RUN_DIR
#注册环境变量  
export PATH  
#虚拟机的一些配置  主要是一个address这个不要重复（防止一个服务器发布多个项目），  
JVM_OPTION="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5789"
#这个主要是创建项目工作的文件夹（如果不存在就创建）  
if [ ! -d "$CACHE_DIR" ]; then
    echo "${CACHE_DIR}文件夹不存在，准备创建!"  
    mkdir -p  "$CACHE_DIR"
    echo "${CACHE_DIR}文件夹创建成功!"  
fi
# 这里是输入运行指令  一般都是 ./socket.sh start ./socket.sh stop ./socket.sh restart   $1是用来接收输入的指令   
case "$1" in
      start)
    echo "Starting luckdraw..."  
    #  这里的声明项目运行的临时目录   日志输出到指定文件  &这个是表示以>>守护进程运行 大概就是支持后台运行的意思  
    java -jar ${JVM_OPTION} -Djava.io.tmpdir="$CACHE_DIR" $RUN_DIR/luckdraw-0.0.1-SNAPSHOT.jar -d"$RUN_DIR">>$RUN_DIR/log &
    # 这里是获取当前项目运行的PID 并写入到pid文件中 为了后面的stop做铺垫  
    echo $! > $RUN_DIR/pid
    ;;
  stop)
    echo "Stopping luckdraw..."  
    #读取pid文件的项目进程ID  
    PID=$(cat $RUN_DIR/pid)
    # 杀死项目的进程  
    kill -9 $PID
    ;;
  restart)
    echo "Stopping luckdraw..."  
    PID=$(cat $RUN_DIR/pid)
    kill -9 $PID
    sleep 2;
    echo "Starting luckdraw..."  
	#java虚拟机启动参数  
	JAVA_OPTS="-Xms512m -Xmx512m -Xmn256m -XX:MetaspaceSize=64m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC -verbose:gc -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -Xloggc:${PROJECT_NAME}-gc.log -Dcom.sun.management.jmxremote.port=${APP_MONITOR_PORT} -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false" 
    java -jar ${JVM_OPTION} -Djava.io.tmpdir="$RUN_DIR/cache" $RUN_DIR/luckdraw-0.0.1-SNAPSHOT.jar -d"$RUN_DIR">> $RUN_DIR/log &
    echo $! > $RUN_DIR/pid
    ;;
 
  *)
    echo "Usage $0 {start|stop|restart}"  
    ;;
esac
#正常运行程序并退出程序  
exit 0
