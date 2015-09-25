<!-- USER INFORMATION MODAL -->
<div class="modal fade" id="game-handshake-modal" aria-hidden="true">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<p class="text-center">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				<h4 class="modal-title" id="game-handshake-modal-title"></h4>
			</div>
			<div class="modal-body text-center">
				<input id="game-handshake-msgTo" type="text" />

				<p class="text-center">
					<button type="button" onclick="agreementToPlay()"
						class="btn btn-danger">Yes</button>
					<button type="button" onclick="refusedToPlay()"
						class="btn btn-default">No</button>
				</p>

			</div>
		</div>
	</div>
</div>