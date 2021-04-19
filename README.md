# MainPlugin
This is the main plugin on the SamiCraft Minecraft server, it handles the Discord bot and the MoneyRPC Balance API

### Discord Bot Features

Discord bot handles various features on the server, some of the most notable are:

- Verification and server whitelisting
- Twitch clips channel filter
- Minecraft server status

#### Command list:

- `!balance` - Displays your balance on the server
- `!botinvite` - Discord invite link for the bot
- `!help` - Command list and description
- `!moneyrpc` - Instructions on how to install the MoneyRPC mod
- `!myid` - Prints out your discord id
- `!seed` - Displays the current world's seed
- `!spawn` - Writes out current world's spawn location
- `!mcstatus` or `!online` - Displays current server status
- `!tutorial` - Youtube playlist containing all the plugin tutorials
- `!verify` - Links your Minecraft and Discord accounts

#### Admin command list:

- `!config` - Changes the server configuration
- `!force` - Verifies or removes users from whitelist
- `!rcon` - Executes a command on the server
- `!whois`  - Retrieves details about a user/player

### MoneyRPC Balance API

You can get real time user balance data from the server using the following endpoint:

- `http://api.samifying.com/v1/balance?id=<discord-id>`

#### Additional API Information

- Rate limit is **1 call per second**
- Returned value is a single string
- User must be verified and must have joined the server once