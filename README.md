# Event Registration 
It's a simple event registration app with CRUD operations, along with authentication and authorization.

## Initial setup
- Create a DB name `eventthinkify`
- For Verification of user email
   ```
    spring.mail.username=your-gmail-id
    spring.mail.password=your-gmail-password
  ```
- In `EmailSenderService.java`, At below line 👇, Use your gmail that provided in `application.properties`
  ```
   message.setFrom("eagerashwani@gmail.com");
  ```

## How to start in your system
After doing the initial setup, Roll up the sleeves 👇
- Clone the repo
- Open in your favourite IDE
- Reload the project (To get all dependencies)

## Dependencies of project
- Spring Starter Web
- Spring Data JPA
- Lombok
- PostgresQL Driver
- Jackson JWT
- Spring Security
- Spring Mail
- ModelMapper

## Flow of the system
- First, You have to create the user.
  - We have 2 methods for signup
    - Normal Signup (email and password)
    - Google Sign up
- Than User have to verifies there email. ie: If user `email is not verified` than user is `not able to login` (**access the system**)
- To verify the user email, An email with OTP was send to user registered mail.
- After verifying the email, Just login with email and password and got the `token`
- User is able to access Event Api based on their role

## APIs
- `Post` Method, Url: `localhost:8081/api/signup`, Body 👇
  ```
  {
    "email": "ashwanithinkify@tidissajiiu.com",
    "password": "password@123",
    "roles":[
        "ATTENDEE",
        "ORGANIZER"
    ]
  }
  ```
  ![alt text](image.png)

- Check the mail id
  
  ![alt text](image-1.png)

- When tried to login without verify the email
  
  ![alt text](image-2.png)

- `Post` Method, Url: `localhost:8081/verify/otp`, Body 👇
  ```
  {
    "email": "ashwanithinkify@tidissajiiu.com",
    "otp": 978081
  }
  ```
    - If OTP expires, expires in 5 min

        ![alt text](image-3.png)

- `Post` Method, Url: `localhost:8081/api/login`, Body 👇
  ```
  {
    "email": "ashwanithinkify@tidissajiiu.com",
    "password": "password@123"
  }
  ```
  ![alt text](image-4.png)

- Check the token data
  ![alt text](image-5.png)

### Event APIs

- `Post` Method, Url: `localhost:8081/event/create-event`, Body 👇 with `token`
  ```
  {
    "name": "Thinkify Labs Conference",
    "description": "A conference about Spring Boot",
    "location": "New York",
    "startDate": "2024-06-15T09:00:00.000Z",
    "endDate": "2024-06-15T17:00:00.000Z",
    "organizerId": 3
  }
  ```
  ![alt text](image-6.png)

- `Get` Method, Url: `localhost:8081/event/get-all-events`, Get All Event Data
  ![alt text](image-7.png)

- `Get` Method, Url: `localhost:8081/event/get-one-event/1`, Get Single Event Data
  ![alt text](image-8.png)

- `Put` Method, Url: `localhost:8081/event/update-event/1`, Body 👇
  ```
  {
    "name": "Thinkify Delhi conference",
    "description": "A conference about Spring Boot",
    "location": "New Delhi",
    "startDate": "2024-06-15T09:00:00.000Z",
    "endDate": "2024-06-15T17:00:00.000Z",
    "organizerId": 3
  }
  ```
  If token is correct
  ![alt text](image-10.png)

  If token is incorrect or tempered
  ![alt text](image-9.png)

- `Delete` Method, Url: `localhost:8081/event/delete-event/3`
  ![alt text](image-11.png)

  After Deleting, Hit Again All Event api
  ![alt text](image-12.png)

## Database Images
- Event Table
  ![alt text](image-13.png)

- User roles
  ![alt text](image-14.png)

- User Table
  ![alt text](image-15.png)

- Verification Table
  ![alt text](image-16.png)

## Improvement to be done
- Use Constructor Injection insteaad of Autowired whereever possible.
- All Exceptions are handled, but not in a proper way
- Response should be in predefined way
- Validation of DTOs
- Token Management
- Use Lookup Table instead of Role Enum
- Testing 