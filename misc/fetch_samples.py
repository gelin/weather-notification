#!/usr/bin/python

import sys
import os
import os.path
import ConfigParser
import httplib

cwd = os.path.abspath((os.path.dirname(sys.argv[0])))

config_file = os.path.join(cwd, 'fetch_samples.conf')
#print 'config file: %s' % config_file
config = ConfigParser.RawConfigParser()
config.read(config_file)

samples_dir = os.path.abspath(os.path.join(cwd, config.get('dirs', 'samples_dir')))
if not os.access(samples_dir, os.W_OK):
    os.mkdirs(samples_dir)

for lang in config.options('languages'):
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
