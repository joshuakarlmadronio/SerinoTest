# SerinoTest

## User Requirements
1. Retrieve list of products from the API
2. Display list of products
    ![UR 1 & 2](files/UR1-2.gif)
3. Show details when user select an item in the list
4. In detail screen, it is in your discretion what data you want to display as long as it looks logical and presentable
    ![UR 3 & 4](files/UR3-4.gif)

## Tech Requirements
1. Source code must be stored in a Git repository (you can send us GitHub or BitBucket repo link)
2. App should cache products (either use local database or local persistent storage)
      - Used Room
3. Should implement pagination, retrieve up to 10 items per page
    ![TR 3](files/TR3.gif)
4. Candidates are free to use any libraries
      - [Picasso](https://github.com/square/picasso) for async image loading
      - [Retrofit2](https://github.com/square/retrofit) for HTTP client
      - [GSON](https://github.com/google/gson) for parsing API response
5. Must be written in Kotlin
6. Project must be able to compile with ./gradlew build
