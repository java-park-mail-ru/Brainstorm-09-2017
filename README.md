# Brainstorm-09-2017

# Команда
* Кирьяненко Александр
* Куклина Нина

# Описание REST API

## 1. Регистрация

  path = /api/users/signup, method = PUT
  
  Входных данные: 
  
    {   
        "login": "qwer",
        "password": "qwerty",
        "email": "qwerty@mail.ru"
    }
           
  Ответ:
    
   200 OK:
   
    {
        "msg": "Successfully registered user"
    }
  
   400 Bad Request:
   
    {
        "error": "There is a user with the same login"
    }
    
    
## 2. Изменение данных

  path = /api/users/edit, method = POST
  
  Входных данные: 
    
    Необязательные поля: email, password
    
    {   
      	"email": "qwer@mail.ru",
     	"password": "qwerty"
    }
       
  Ответ:
    
    {"status": "..."}
    
## 3. Полученние данных о текущем пользователе

  path = /api/users/me, method = GET
  
  Ответ:
    
    {
      "id": "...",
      "login": "...",
      "email": "...",
    }
    

    
## 4. Вход

  path = /api/users/signin, method = POST
  
  Входных данные: 
    
    {   
      	"login": "qwer",
     	"password": "qwerty"
    }
   
  Ответ:
    
    {"status": "..."}
    
## 5. Выход

  path = /api/users/logout, method = GET
   
  Ответ:
    
    {
        "msg": "Successfully logout"
    }
