from pprint import pprint
from Crypto.Cipher import AES
import json
import rsa
import rsa.randnum
import pymongo
from pymongo import MongoClient
user = raw_input()
message = raw_input()
arr=message.split("g")
with open("privkey.key","r") as cc:
	bb=json.loads(cc.read())
	privkey=rsa.PrivateKey(bb[0],bb[1],bb[2],bb[3],bb[4])
aes_after=rsa.decrypt(arr[0].decode("hex"),privkey)
decryption_suite = AES.new(aes_after, AES.MODE_CFB, 'This is an IV456')
dec = decryption_suite.decrypt(arr[1].decode("hex"))
print "Plain message: "+ dec