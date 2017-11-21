import json
from pprint import pprint
from Crypto.Cipher import AES
import rsa
import rsa.randnum
import pymongo
from pymongo import MongoClient
#print "User:"
user = raw_input()
#print "Mail:"
message = raw_input()
#client = MongoClient('mongodb://localhost:27017/')
#db = client.pubkeys
#p=db.users
#c=db.secure
#pub=[]
#p.remove()
#if(p.count({"name":user})<=0):
#	print "New User. Public key pair generating"
(pubkey, privkey) = rsa.newkeys(1024)
#	pub.append(str(pubkey.n))
#	pub.append(str(pubkey.e))
#	print pub
#	p.insert_one({"name":user,"pubkey":pub})
#else:
#	print "Existing User. Fetching public key from db"

#	pubkey = rsa.PublicKey(int(p.find_one({"name":user})["pubkey"][0]),int(p.find_one({"name":user})["pubkey"][1]))
#	print pubkey
aes_key = rsa.randnum.read_random_bits(128)
encryption_suite = AES.new(aes_key, AES.MODE_CFB, 'This is an IV456')
cipher_text = encryption_suite.encrypt(message)
encrypted_aes_key = rsa.encrypt(aes_key, pubkey)
decrypted_aes_key = rsa.decrypt(encrypted_aes_key,privkey)
final=encrypted_aes_key.encode("hex")+"g"+cipher_text.encode("hex")
with open("privkey.key","w") as cc:
	a=[]
	a.append(privkey.n)
	a.append(privkey.e)
	a.append(privkey.d)
	a.append(privkey.p)
	a.append(privkey.q)
	cc.write(json.dumps(a))
arr=final.split("g")
#print arr[0]
#print encrypted_aes_key
aes_after=rsa.decrypt(arr[0].decode("hex"),privkey)
#print aes_key.encode("hex")

decryption_suite = AES.new(aes_after, AES.MODE_CFB, 'This is an IV456')
dec = decryption_suite.decrypt(arr[1].decode("hex"))

print user
print final

