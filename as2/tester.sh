#! /bin/sh
path=$(cd $(dirname $0) && pwd )
echo "================================="
echo "==    Compile main program     =="
echo "================================="
cd $path/../
make default
export CLASSPATH=$CLASSPATH:$path/../
cd $path
echo "================================="
echo "==   Start assignment2 test    =="
echo "================================="
for i in {2..16}
do
	echo "Question $i: "
	if [ -a ./solutions/$i.sql ];then
		java -ea db61b.Main < ./solutions/$i.sql > msg.log
		if diff -qw out.db ./answers/$i.db > /dev/null;then
			echo "  Passed."
		else
			echo "  Failed."
		fi
	else
		echo "  Skip."
	fi
	echo "============================"
done