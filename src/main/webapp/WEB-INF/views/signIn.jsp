<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<jsp:include page="includes/header.jsp" />
<div class="container-fluid">
<jsp:include page="includes/menu.jsp"></jsp:include>
	<div class="col-md-10 col-md-offset-1 ">

		<br />
		<div class="row contentWrapper">
			<div class="sign-login-form">
				<h1 class="text-center">create your account</h1>

				<hr />
				<jsp:include page="includes/forms/signInForm.jsp" />

				<hr />
			</div>
		</div>
	</div>
</div>
<jsp:include page="includes/footer.jsp" />