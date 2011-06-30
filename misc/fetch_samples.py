#!/usr/bin/python

import sys
import os
import os.path
import ConfigParser
import csv
import httplib

cwd = os.path.abspath((os.path.dirname(sys.argv[0])))

config_file = os.path.join(cwd, 'fetch_samples.conf')
#print 'config file: %s' % config_file
config = ConfigParser.RawConfigParser()
config.read(config_file)

samples_dir = os.path.abspath(os.path.join(cwd, config.get('dirs', 'samples_dir')))
if not os.access(samples_dir, os.W_OK):
    os.mkdirs(samples_dir)
    
lang_file = os.path.abspath(os.path.join(cwd, config.get('languages', 'lang_file')))
lang_reader = csv.reader(open(lang_file), delimiter='|')

for row in lang_reader:
    lang = row[2]
    if not lang:
        continue
    sample_file = os.path.join(samples_dir, ('%s.xml' % lang))
    sample_path = '/ig/api?weather=Omsk&hl=%s' % lang
    print '%s -> %s' % (sample_path, sample_file)
    connection = httplib.HTTPConnection("www.google.com")
    connection.request("GET", sample_path)
    response = connection.getresponse()
    file = open(sample_file, 'w')
    file.write(response.read())
    file.close()
    connection.close()
