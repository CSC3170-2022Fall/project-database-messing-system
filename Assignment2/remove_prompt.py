import os
import sys
with open(os.path.join(sys.path[0],"msg.log"),"r") as f:
	main_body = []
	idx = 0
	for line in f:
		idx += 1
		if idx >= 5:
			main_body.append(line)
	with open(os.path.join(sys.path[0],"msg.log"),"w") as ff:
		for line in main_body:
			ff.write(line)
