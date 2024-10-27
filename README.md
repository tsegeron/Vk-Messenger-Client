# VK Messenger Client

VK Messenger Client is a modular Android application, the UI design of which was inspired by Telegram.  

> Under development  
> Currently, authorization and the Friends screen are implemented.

## Project structure
The project is devided into 2 primary modules:
1. _Core Modules_ (contains shared resources and utilities used across various features):
   * _data_: Manages data models and repos, handling data persistence and network requests
   * _ui_: Provides generic UI components
   * _common_: Provides common extensions, annotations etc
   * _datastore, notifications, settings_: Handle specific functionalities

2. _Feature Modules_ (self-contained features/screens):
   * _auth_: Manages user authentication via VK
   * _friends_: Manages friend-related features
   * _chats, chat, chatinfo, settings_


