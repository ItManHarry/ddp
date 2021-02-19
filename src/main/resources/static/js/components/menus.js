var SystemMenu = {
	template:`
		<div>
			<el-tooltip style="margin:4px;" effect="light" placement="bottom-start">
				<div slot="content">
					<div v-for = "(menu,index) in menus">
						<hr>
						<el-link :underline="false" :href = "menu.url" :key = "index"><i class = "fa fa-angle-double-right"></i>&nbsp;&nbsp;{{menu.name}}</el-link>
					</div>
				</div>
				<el-button icon="el-icon-menu"></el-button>
			</el-tooltip>
		</div>
	`,
	data:function(){
		return {
			menus:[
				{name:'项目管理', url:'/pm/biz/pro/list'},
				{name:'Issue管理', url:'/pm/biz/pro/issue/list'},
				{name:'系统管理', url:'/pm/sys/dict/list'},
			]
		}
	},
	props:[
		
	],
	mounted:function(){
		//alert('System menu mounted ...')
	}
}