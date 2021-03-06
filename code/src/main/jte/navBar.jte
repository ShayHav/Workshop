@param Presentation.Model.PresentationUser user
@param String title
<!doctype html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.min.js"
            integrity="sha384-Atwg2Pkwv9vp0ygtn1JAojH0nYbwNJLPhwyoVbhoPwBhjQPR5VtM2+xf0Uwh9KtT"
            crossorigin="anonymous"></script>
    <title>${title}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script>
        const messagesSocket = new WebSocket('ws://localhost:${Presentation.Main.port}/users/${user.getUsername()}/messages/getCount');
        messagesSocket.onmessage = (event) => {
            let count = event.data;
            let counter = document.querySelector('#messageCount');
            counter.textContent = count === 0 || count === '0' ? '' : count;
        }
        const activeUserSocket = new WebSocket('ws://localhost:${Presentation.Main.port}/users/${user.getUsername()}/active');

        setInterval(()=>{
            activeUserSocket.send("I am active");
        }, 10000);

    </script>
</head>
<body>
<nav class="sticky-top navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <div class="navbar-brand">
            <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-shop-window"
                 viewBox="0 0 16 16">
                <path d="M2.97 1.35A1 1 0 0 1 3.73 1h8.54a1 1 0 0 1 .76.35l2.609 3.044A1.5 1.5 0 0 1 16 5.37v.255a2.375 2.375 0 0 1-4.25 1.458A2.371 2.371 0 0 1 9.875 8 2.37 2.37 0 0 1 8 7.083 2.37 2.37 0 0 1 6.125 8a2.37 2.37 0 0 1-1.875-.917A2.375 2.375 0 0 1 0 5.625V5.37a1.5 1.5 0 0 1 .361-.976l2.61-3.045zm1.78 4.275a1.375 1.375 0 0 0 2.75 0 .5.5 0 0 1 1 0 1.375 1.375 0 0 0 2.75 0 .5.5 0 0 1 1 0 1.375 1.375 0 1 0 2.75 0V5.37a.5.5 0 0 0-.12-.325L12.27 2H3.73L1.12 5.045A.5.5 0 0 0 1 5.37v.255a1.375 1.375 0 0 0 2.75 0 .5.5 0 0 1 1 0zM1.5 8.5A.5.5 0 0 1 2 9v6h12V9a.5.5 0 0 1 1 0v6h.5a.5.5 0 0 1 0 1H.5a.5.5 0 0 1 0-1H1V9a.5.5 0 0 1 .5-.5zm2 .5a.5.5 0 0 1 .5.5V13h8V9.5a.5.5 0 0 1 1 0V13a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V9.5a.5.5 0 0 1 .5-.5z"/>
            </svg>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
        </div>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" aria-current="page" href="/">Home</a>
                </li>
                @if (user.isLoggedIn())
                    <li class="nav-item">
                        <button id="createShopBtn" data-bs-toggle="collapse" data-bs-target="#createShop"
                                aria-expanded="false" aria-controls="createShop" class="nav-link bg-dark border-0">
                            Create Shop
                        </button>
                    </li>
                @else
                    <a class="nav-link" href="/users/login">Create Shop</a>
                @endif
            </ul>
            <div class="me-auto col-6">
                <form class="d-flex" method="get" action="/search">
                    <div class="col-3">
                        <select class="form-select border-end-0 rounded-start" name="searchBy"
                                aria-label="Disabled select example">
                            <option selected value="products">Product's Name</option>
                            <option value="category">Category</option>
                            <option value="keyword">Keyword</option>
                        </select>
                    </div>
                    <input class="form-control me-2" name="query" type="search" placeholder="Search"
                           aria-label="Search">
                    <button class="btn btn-outline-success" type="submit">Search</button>
                </form>
            </div>
            <div class="me-3">
                <a href="/users/${user.getUsername()}/messages" class="nav-link bg-dark position-relative">
                  <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-envelope" viewBox="0 0 16 16">
                        <path d="M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V4Zm2-1a1 1 0 0 0-1 1v.217l7 4.2 7-4.2V4a1 1 0 0 0-1-1H2Zm13 2.383-4.708 2.825L15 11.105V5.383Zm-.034 6.876-5.64-3.471L8 9.583l-1.326-.795-5.64 3.47A1 1 0 0 0 2 13h12a1 1 0 0 0 .966-.741ZM1 11.105l4.708-2.897L1 5.383v5.722Z"/>
                    </svg>
                    <span id='messageCount'
                          class="position-absolute top-35 start-100 translate-middle badge rounded-pill bg-danger"></span>
                </a>
            </div>
            <div class="col-2">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <div class="dropdown">
                        <button class="nav-link bg-dark border-0 dropdown-toggle" type="button" id="dropdownMenuButton2"
                                data-bs-toggle="dropdown" aria-expanded="false">
                            Account
                        </button>
                        <ul class="dropdown-menu dropdown-menu-dark" aria-labelledby="dropdownMenuButton2">
                            @if(!user.isLoggedIn())
                                <li><a class="dropdown-item-text" href="/users/login">Sign in</a></li>

                            @else
                                <li><p class="dropdown-item-text">Signed in as <span style="font-weight: bold; color: #59c03c ">${user.getUsername()}</span></p></li>

                                @if(user.isAdmin())
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="/admin/${user.getUsername()}/systemMonitor">System Information</a></li>
                                    <li><a class="dropdown-item" href="/admin/${user.getUsername()}/sales">All Sales History</a></li>
                                @endif
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="/users/${user.getUsername()}/orders">My Orders</a></li>
                                <li><a class="dropdown-item" href="/users/${user.getUsername()}/shops">My Shops</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <form method="post" action="/users/${user.getUsername()}/logout">
                                    <button class="dropdown-item">Logout</button>
                                </form>
                            @endif


                        </ul>
                    </div>
                    <li class="nav-item">
                        <a class="nav-link" href="/users/${user.getUsername()}/cart">
                            <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor"
                                 class="bi bi-cart-check" viewBox="0 0 16 16">
                                <path d="M11.354 6.354a.5.5 0 0 0-.708-.708L8 8.293 6.854 7.146a.5.5 0 1 0-.708.708l1.5 1.5a.5.5 0 0 0 .708 0l3-3z"/>
                                <path d="M.5 1a.5.5 0 0 0 0 1h1.11l.401 1.607 1.498 7.985A.5.5 0 0 0 4 12h1a2 2 0 1 0 0 4 2 2 0 0 0 0-4h7a2 2 0 1 0 0 4 2 2 0 0 0 0-4h1a.5.5 0 0 0 .491-.408l1.5-8A.5.5 0 0 0 14.5 3H2.89l-.405-1.621A.5.5 0 0 0 2 1H.5zm3.915 10L3.102 4h10.796l-1.313 7h-8.17zM6 14a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm7 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0z"/>
                            </svg>
                        </a>
                    </li>

                </ul>
            </div>
        </div>
    </div>
</nav>

@if (user.isLoggedIn())
<div class="collapse container border border-1 mt-3 col-6 rounded" id="createShop">
    <form class="mt-3" method="post" action="/shops">
        <h3 class="fs-5">create new shop in market:</h3>
        <div class=" mb-2">
            <input type="text" name="shopName" class="form-control" id="shopName" placeholder="shopName">
        </div>
        <div class="mb-2">
            <input type="text" name="description" class="form-control" id="description" placeholder="description">
        </div>
        <button class="btn btn-success" type="submit">create new shop</button>
    </form>
</div>
@endif
