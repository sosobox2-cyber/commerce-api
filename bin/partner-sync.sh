#!/bin/sh
############################################################
#
# 프로그램유형: Start(shell)
# 프로그램명: partner-sync.sh
# Version: 1.0
# 작성일: 2022/02/04
# 작성자: CommerceWare
# 설명: Partner Product Change Data Sync
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

# Only set PARTNER_SYNC_HOME if not already set
[ -z "$PARTNER_SYNC_HOME" ] && PARTNER_SYNC_HOME=`cd "$PRGDIR/" ; pwd`
BASEDIR="$PARTNER_SYNC_HOME"
PARTNER_SYNC_PID=$BASEDIR/partner-sync.pid

start() {

   if [ -e "$PARTNER_SYNC_PID" ]
   then
       PID=`cat $PARTNER_SYNC_PID`
       RUNPID=`ps -aux | awk '{print $2}' | grep $PID`

       if [ "$PID" = "$RUNPID" ]; then
         echo "Partner Sync (pid $PID) already running"
         RETVAL=$?
         return $RETVAL
       fi
   fi

   cd $BASEDIR

   echo -n 'Partner Sync process START ...'
   java -Xms256m -Xmx512m \
        -DName=CommerceWare_PartnerSync_v1.0 \
        -DModuleName=PartnerSyncApplication \
        -Dfile.encoding=UTF-8 \
        -Dapp.bin=/app/partner/sync/bin \
        -Dspring.profiles.active=dev \
        -classpath '.' \
        -jar stoa-partner-sync-1.0.jar &

   #-jar stoa-partner-sync-1.0.jar > /app/partner/sync/logs/partner-sync.log &


   echo
   JAVA_PID=$!
      echo $JAVA_PID > $PARTNER_SYNC_PID

   RETVAL=$?

    return $RETVAL
}


stop() {

    if [ -e $PARTNER_SYNC_PID ]; then
       echo "Partner Sync Killing: `cat $PARTNER_SYNC_PID`"
       kill -9 `cat $PARTNER_SYNC_PID`
       RETVAL=$?
       rm -f $PARTNER_SYNC_PID
    else
       echo "Kill failed: \$PARTNER_SYNC_PID not set"
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
