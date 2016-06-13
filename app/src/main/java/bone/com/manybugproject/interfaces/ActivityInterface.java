package bone.com.manybugproject.interfaces;

/**
 * activity类规范
 * 
 * Package: com.ichsy.minsns.interfaces  
 *  
 * File: ActivityInterface.java   
 *  
 * @author: 赵然   Date: 2015-4-24  
 *
 * Modifier： Modified Date： Modify： 
 *
 */
public interface ActivityInterface {

	/**
	 * 初始化控件
	 */

	void loadLayout();
	/**
	 * 逻辑代码
	 */

	void logic();

	/**
	 * 监听事件
	 */
	void loadListener();

	/**
	 * 请求代码
	 */

	void request();

	/**
	 * 点击请求代码
	 */

	void clickRequest();



}
