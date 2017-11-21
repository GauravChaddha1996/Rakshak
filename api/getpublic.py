from flask import Flask
import os
import subprocess
import sys
import json
import requests
from flask import request
import subprocess
app = Flask(__name__)
@app.route('/getpublic')
def addnew():
	
	user=request.args.get('email_id')
	cmd='''curl -X GET -o result -k -i -H "Content-Type: application/json"\
 -H "x-apikey: 9b63be897efd04ca200d56700a7f1bdc93247"\
 -G 'https://pgpusers-af24.restdb.io/rest/user-details-test' -d 'q={"email_id":'''+user+'''}' '''


	#cmd='curl -o result -X POST --user us@221b.tk:211B_sih2017 -d "email='+user+'@221b.tk" -d "password='+pwd+'" https://box.221b.tk/admin/mail/users/add > result'
	
	os.system(cmd)
	s=open("result","r").read()
	open("out","w").write(json.dumps(s))
	temp=s.find("[")
	#print s[temp:]
	return s[temp:]
	#return "hello world!!"
if __name__ == '__main__':
	app.run(host='0.0.0.0',port=5000)