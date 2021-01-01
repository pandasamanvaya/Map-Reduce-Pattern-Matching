import numpy as np

Symbol = ['A', 'T', 'G', 'C']
key = ""
key_l = 20; file_len = int(5e7)
for i in range(key_l):
	key += Symbol[np.random.randint(0,len(Symbol))]

a = np.arange(file_len)
np.random.shuffle(a)
occ = 100
pos = a[:occ]
pos.sort()

Symbol.append('*')
Symbol.append('$')

file = ""; j = 0
for i in range(file_len):
	if i%int(1e7) == 0:
		print(i//1e7)
	if j < occ and pos[j] == i:
		file += key
		j += 1
	else:
		file += Symbol[np.random.randint(0,len(Symbol))]

fs = open("sample_temp.txt", 'w')
fs.write(file)
fs.close

fk = open("key_temp.txt", 'w')
fk.write(key)
fk.close