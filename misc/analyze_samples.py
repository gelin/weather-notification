#!/usr/bin/python

import sys
import os
import os.path
import ConfigParser
import xml.dom.minidom

cwd = os.path.abspath((os.path.dirname(sys.argv[0])))

config_file = os.path.join(cwd, 'analyze_samples.conf')
#print 'config file: %s' % config_file
config = ConfigParser.RawConfigParser()
config.read(config_file)

samples_dir = os.path.abspath(os.path.join(cwd, config.get('dirs', 'samples_dir')))
    
for file in os.listdir(samples_dir):
    lang = os.path.splitext(file)[0]
    print lang
    dom = xml.dom.minidom.parse(os.path.abspath(os.path.join(samples_dir, file)))
    unit = dom.getElementsByTagName('unit_system').getAttribute('data')
    wind = dom.getElementsByTagName('wind_condition').getAttribute('data')
    humidity = dom.getElementsByTagName('humidity').getAttribute('data')
    print '%s\t%s\t%s\t%s' % (lang, unit, wind, humidity)
