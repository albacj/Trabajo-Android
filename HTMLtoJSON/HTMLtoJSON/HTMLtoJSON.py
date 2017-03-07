from bs4 import BeautifulSoup
import urllib3
import json

http = urllib3.PoolManager()
url = "http://siu.ctas.ctan.es/es/horarios_lineas_tabla.php?from=1&linea=275"
response = http.request('GET', url)
soup = BeautifulSoup(response.data)

print(soup)