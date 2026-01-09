#!/bin/sh
############################################################
#
# 프로그램유형: Start(shell)
# 프로그램명: partner-coupang.sh
# Version: 1.0
# 작성일: 2022/04/21
# 작성자: CommerceWare
# 설명: Partner Product Data Transfer to Coupang
#
############################################################

. /etc/profile

# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

# Get standard environment variables
PRGDIR=`dirname "$PRG"`

# Only set PARTNER_COUPANG_HOME if not already set
[ -z "$PARTNER_COUPANG_HOME" ] && PARTNER_COUPANG_HOME=`cd "$PRGDIR/" ; pwd`
BASEDIR="$PARTNER_COUPANG_HOME"
PARTNER_COUPANG_PID=$BASEDIR/partner-coupang.pid

start() {

   if [ -e "$PARTNER_COUPANG_PID" ]
   then
       PID=`cat $PARTNER_COUPANG_PID`
       RUNPID=`ps -aux | awk '{print $2}' | grep $PID`

       if [ "$PID" = "$RUNPID" ]; then
         echo "Partner Coupang (pid $PID) already running"
         RETVAL=$?
         return $RETVAL
       fi
   fi

   cd $BASEDIR

   echo -n 'Partner Coupang process START ...'
   java -Xms256m -Xmx512m \
        -DName=CommerceWare_PartnerCoupang_v1.0 \
        -DModuleName=PartnerCoupangApplication \
        -Dfile.encoding=UTF-8 \
        -Dapp.bin=/app/partner/coupang/bin \
        -Dspring.profiles.active=dev \
        -classpath '.' \
        -jar stoa-partner-coupang-1.0.jar &


   echo
   JAVA_PID=$!
      echo $JAVA_PID > $PARTNER_COUPANG_PID

   RETVAL=$?

    return $RETVAL
}


stop() {

    if [ -e $PARTNER_COUPANG_PID ]; then
       echo "Partner Coupang Killing: `cat $PARTNER_COUPANG_PID`"
       kill -9 `cat $PARTNER_COUPANG_PID`
       RETVAL=$?
       rm -f $PARTNER_COUPANG_PID
    else
       echo "Kill failed: \$PARTNER_COUPANG_PID not set"
    fi
    return $RETVAL
}

case "$1" in
  start)
        start
        ;;
  stop)
        stop
        ;;
  *)
        echo $"Usage: $0 {start|stop}"
        exit 1
esac

exit $?
