Simple expenses tracker with additional functionality of categorization and retrieving Bitcoin to USD exchange rate from network

# Stack
Kotlin, Compose, MVVM, DataStore, Room, Retrofit, Kotlin Coroutines, JUnit

# Screenshots
## Screen 1

The first screen displays the balance and a list of all transactions grouped by day and sorted from newest to oldest.
We use pagination of 20 transactions when displaying.

In addition, we display the Bitcoin exchange rate against the dollar (it is updated every session, but not more often than once an hour) and
the “Add transaction” button, which leads to Screen 2.

<img src="https://github.com/user-attachments/assets/e8ea2c6b-c1a0-48fb-adf1-bac2d61fdc85" width="300">
<img src="https://github.com/user-attachments/assets/20588f46-b4ec-465a-bcf6-0da46d53f9cc" width="300">

## Screen 2

The second screen displays the transaction amount input field, category selection (groceries, taxi, electronics, restaurant, other), and the “Add” button.

<img src="https://github.com/user-attachments/assets/25ff6015-792c-4420-acc9-a01ec1d529eb" width="300">

