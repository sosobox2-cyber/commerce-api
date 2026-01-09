#!/bin/sh
############################################################
#
# 프로그램유형: Start(shell)
# 프로그램명: partner-cdc.sh
# Version: 1.0
# 작성일: 2022/01/17
# 작성자: CommerceWare
# 설명: Partner Product Change Data Capture
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

# Only set PARTNER_CDC_HOME if not already set
[ -z "$PARTNER_CDC_HOME" ] && PARTNER_CDC_HOME=`cd "$PRGDIR/" ; pwd`
BASEDIR="$PARTNER_CDC_HOME"
PARTNER_CDC_PID=$BASEDIR/partner-cdc.pid

start() {

   if [ -e "$PARTNER_CDC_PID" ]
   then
       PID=`cat $PARTNER_CDC_PID`
       RUNPID=`ps -aux | awk '{print $2}' | grep $PID`

       if [ "$PID" = "$RUNPID" ]; then
         echo "Partner CDC (pid $PID) already running"
         RETVAL=$?
         return $RETVAL
       fi
   fi

   cd $BASEDIR

   echo -n 'Partner CDC process START ...'
   java -Xms256m -Xmx512m \
        -DName=CommerceWare_PartnerCDC_v1.0 \
        -DModuleName=PartnerCdcApplication \
        -Dfile.encoding=UTF-8 \
        -Dapp.bin=/app/partner/cdc/bin \
        -Dspring.profiles.active=dev \
        -classpath '.' \
        -jar stoa-partner-cdc-1.0.jar &

   #-jar stoa-partner-cdc-1.0.jar > /app/partner/cdc/logs/partner-cdc.log &


   echo
   JAVA_PID=$!
      echo $JAVA_PID > $PARTNER_CDC_PID

   RETVAL=$?

    return $RETVAL
}


stop() {

    if [ -e $PARTNER_CDC_PID ]; then
       echo "Partner CDC Killing: `cat $PARTNER_CDC_PID`"
       kill -9 `cat $PARTNER_CDC_PID`
       RETVAL=$?
       rm -f $PARTNER_CDC_PID
    else
       echo "Kill failed: \$PARTNER_CDC_PID not set"
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
