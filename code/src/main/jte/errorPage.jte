@param String errorMessage
@param int status

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Oops</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body>

<div class="container mt-5">
    <b><h1 class="fs-2">Oops..Looks like something bad happened</h1></b>
    <div class="container row">
        <div class="col">
            <p class="fs-4">${errorMessage}</p>
            <b><p class="fs-4">Error Code: ${status}</p></b>
            <a class="nav-link" aria-current="page" href="/">Return to Homepage</a>
        </div>
        <div class="col">
            <img src="https://a0.muscache.com/airbnb/static/error_pages/404-Airbnb_final-d652ff855b1335dd3eedc3baa8dc8b69.gif"
                 width="313" height="428" alt="Girl has dropped her ice cream.">
        </div>
    </div>
</div>
</body>
</html>