一个带有菜单的根据点击View 显示菜单位置的的android的popupwindow的使用。   <br/>

<img src="https://github.com/hpuzzuldx/PulldownPopupWindow/blob/master/images/1.png"  width="270px"><br/>
<img src="https://github.com/hpuzzuldx/PulldownPopupWindow/blob/master/images/2.png"  width="270px"><br/>
<img src="https://github.com/hpuzzuldx/PulldownPopupWindow/blob/master/images/3.jpg"  width="270px"><br/>
<img src="https://github.com/hpuzzuldx/PulldownPopupWindow/blob/master/images/4.jpg"  width="270px"><br/>
<img src="https://github.com/hpuzzuldx/PulldownPopupWindow/blob/master/images/5.jpg"  width="270px"><br/>
<img src="https://github.com/hpuzzuldx/PulldownPopupWindow/blob/master/images/6.jpg"  width="270px"><br/>

##使用方法##
###初始化###
     '''
       PullDownMenu dropPopMenu = new PullDownMenu(this);
        dropPopMenu.setTriangleIndicatorViewColor(Color.WHITE);
        dropPopMenu.setBackgroundResource(R.drawable.dislike_popmenu_white_shap);

        dropPopMenu.setMenuList(getIconMenuList());
        dropPopMenu.show(view);'''

###数组设置###
'''
         private ArrayList<MenuItem> getIconMenuList() {
                ArrayList<MenuItem> list = new ArrayList<>();
                list.add(new MenuItem( 1, "选项1"));
                list.add(new MenuItem( 2, "选项2"));
                list.add(new MenuItem( 3, "选项3"));
                list.add(new MenuItem( 4, "选项4"));
                list.add(new MenuItem( 5, "选项5"));
                return list;
            }
            '''
