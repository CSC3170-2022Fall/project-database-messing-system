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
		python sort.py
		out="out_sorted.db"
		if [ $i == 7 ]; then
			out="out.db"
		fi
		if diff -qw $out ./answers/$i.db > /dev/null;then
			echo "  Passed."
		else
			echo "  Failed."
		fi
	else
		echo "  Skipped."
	fi
	echo "============================"
done