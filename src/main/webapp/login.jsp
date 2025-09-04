<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
</head>
	
<body>
<br>
<div class="container">
	<div class="row">
		<div class="col">
			<hr>
			<h3>Login</h3>
			<hr>
			<form action="<%=request.getContextPath()%>/login" method="POST">
					 <div class="form-floating mb-3">
                        <input name="email" type="email" class="form-control" id="floatingEmail" required>
                        <label for="floatingEmail">E-mail</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input name="senha" type="password" class="form-control" id="floatingSenha" required>
                        <label for="floatingSenha">Senha</label>
                    </div>
					<button class="btn btn-primary" type="submit">Entrar</button>
					<div class="mb-3 text-end">
    					<a href="recuperar-senha.jsp" class="small">Esqueceu a senha?</a>
					</div>
			</form>
			<br>
		</div>
		
	</div>
</div>
<!-- JavaScript Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
</body>
</html>