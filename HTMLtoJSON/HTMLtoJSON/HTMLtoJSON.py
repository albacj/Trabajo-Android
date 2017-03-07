
import urllib3

html_data = "http://siu.ctas.ctan.es/es/horarios_lineas_tabla.php?from=1&linea=275"
url = urllib3.urlopen(html_data)
content = url.read()
soup = BeautifulSoup(content)

print(soup)