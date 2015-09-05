<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<br />
<footer class="footer">
	<span class="author">author: Marcin Kuzdowicz</span> <span
		class="appTitle">chess application</span>
</footer>
<script>
<!-- MAKE CURRENT PAGE BUTTON ACTIVE AND DEFULT PAGE ON START ACTIVE -->
	$(document).ready(
			function() {

				//default menu item
				$('#navButtons li:eq( 0 )').addClass('active')

				var url = window.location.href;
				$("#navButtons a").each(function() {

					if (url.match(this.href)) {
						$('#navButtons li:eq( 0 )').removeClass('active')
						$(this).closest("li").addClass("active");
					}
				});
				if (window.location.href == "http://" + window.location.host
						+ "/admin/users"
						|| window.location.href == "https://"
								+ window.location.host + "/admin/users") {
					$('#usersTableForDataTableJS').DataTable();
					$('.paginate_button.current').attr('style',
							'color: white !important');

				}

			});
</script>
</body>
</html>