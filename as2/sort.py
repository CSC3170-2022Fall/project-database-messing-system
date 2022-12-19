import os
import sys
with open(os.path.join(sys.path[0],"out.db"),"r") as f:
	title = []
	sortfile = []
	id = 0
	for line in f:
		id += 1
		if id <= 2:
			title.append(line)
		else:
			sortfile.append(line)
	sortfile.sort()
	with open(os.path.join(sys.path[0],"out_sorted.db"),"w") as ff:
		for line in title:
			ff.write(line)
		for line in sortfile:
			ff.write(line)
