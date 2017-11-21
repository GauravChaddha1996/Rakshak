from flask import Flask
import os
import subprocess
import sys
import json
import requests
from flask import request
import subprocess
app = Flask(__name__)
@app.route('/addnew')
def addnew():
	
	user=request.args.get('user')
	pwd=request.args.get('pwd')
	print user
	print pwd
	cmd='curl -o result -X POST --user us@221b.tk:211B_sih2017 -d "email='+user+'@221b.tk" -d "password='+pwd+'" https://box.221b.tk/admin/mail/users/add > result'
	
	os.system(cmd)
	s=open("result","r").read()
	return s
	#return "hello world!!"
if __name__ == '__main__':
	app.run(host='0.0.0.0')