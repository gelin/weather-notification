#!/usr/bin/python

import sys
import os
import os.path
import ConfigParser
from xml.etree import ElementTree

cwd = os.path.abspath((os.path.dirname(sys.argv[0])))

config_file = os.path.join(cwd, 'analyze_samples.conf')
#print 'config file: %s' % config_file
config = ConfigParser.RawConfigParser()
config.read(config_file)

samples_dir = os.path.abspath(os.path.join(cwd, config.get('dirs', 'samples_dir')))

for file in os.listdir(samples_dir):
    (lang, encoding) = file.split('.')[0:2]
    #print lang
    #content = unicode(open(os.path.abspath(os.path.join(samples_dir, file))).read(), encoding)
    #print content
    try:
        xml = ElementTree.parse(os.path.abspath(os.path.join(samples_dir, file)),
                            ElementTree.XMLParser(encoding=encoding))
    except Exception as e:
        print file, e
        continue
    unit = next(xml.iter('unit_system')).get('data')
    wind = next(xml.iter('wind_condition')).get('data')
    humidity = next(xml.iter('humidity')).get('data')
    print ('%s\t%s\t%s\t%s' % (lang, unit, wind, humidity)).encode('utf-8')
