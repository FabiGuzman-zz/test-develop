

# 1) Es posible probar el programa desde la herramienta postman de la siguiente manera:

# 2) El endpoint del servicio es https://8n9kwx0rm6.execute-api.us-east-2.amazonaws.com/dev/binary-tree

# 3) El bear token de autorización se genera con el siguiente comando

# curl -X POST --user 512vej77qs4b0r76muv9tesn4c:u5mcgms6r90pm7864n5c6ggcai8nmsle8a6vgnadtetrr6o5qs0 'https://bb-dev-cc.auth.us-east-2.amazoncognito.com/oauth2/token?grant_type=client_credentials' -H 'Content-Type: application/x-www-form-urlencoded'

# 4) Se debe ingresar el header X-TreeId:identificadorEntero donde identificadorEntero es un número con el cual 
#    se guardará el arbol e igualmente se recuperará cuando se desee buscar el ancestro común más cercano de dos nodos

#    Ejemplo de headers:
#                      Content-Type:application/json
#                      X-TreeId:2
 
# 5) En el request se debe pasar una lista, la cual contiene todos los valores del arbol binario linea a linea por niveles, 
#    iniciando desde el nivel 0 o nodo raiz. En caso de nodos nulos en alguna posición del nivel, se debe pasar null

#    Ejemplo de request: {
#                           "treeData": ["67","39","23","null","45","5","null"]
#                        }

#                                         67
#                                    39       23
#                                null  45  5     null

# 6) Cuando se quiera calcular el ancestro común más cercano de dos nodos, la lista debe contener los dos nodos y se debe pasar 
#    en el header X-TreeId, el id del arbol que previamente fue construido

#    Ejemplo de request para calcular el ancestro: {
#                                                     "treeData": ["39","5"]
#                                                  }