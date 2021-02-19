
var SystemHeader = {
	template:`
		<div>
			<h1>Program system header {{title}} ...</h1>
		</div>
	`,
	props:[
		'title'
	],
	mounted:function(){
		alert('System header mounted ...')
	}
}