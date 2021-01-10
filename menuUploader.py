import json
import requests

url = "https://superb-heaven-298114-default-rtdb.europe-west1.firebasedatabase.app/"

def send_res(res):
    headers = {
    'Content-Type': 'application/json'
    }

    response = requests.request("PATCH", f"{url}/ristoranti.json", headers=headers, data=res)

    print(response.text)

def send_menu(menu):
    headers = {
    'Content-Type': 'application/json'
    }

    response = requests.request("PATCH", f"{url}/menu.json", headers=headers, data=menu)

    print(response.text)

def load_menu():
    with open('restaurant-menu.json','r') as f:
        j = json.load(f)['array']
        infoJson = j['restaurant-info']
        info = {'nome':infoJson['name'],'localita':'VE','indirizzo':infoJson['address']}
        restaurantID = f"{info['nome']}{info['localita']}"

        menuJson = j['categorys']
        menu = {}
        for cat in menuJson:
            catName = cat['name'].replace(".","")
            menu[catName] = {}
            for p in cat['menu-items']:
                menu[catName][p['id']] = {'nome':p['name'],'descrizione':p['description'],'prezzo':float(p['price'])}
        
        send_res(json.dumps({restaurantID:info}))
        send_menu(json.dumps({restaurantID:menu}))

if __name__ == "__main__":
    load_menu()