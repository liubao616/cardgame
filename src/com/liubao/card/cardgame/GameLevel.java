package com.liubao.card.cardgame;

/**
 * 游戏关卡
 * 
 * @author Administrator
 * 
 */
public class GameLevel {

	public static final int TOTALLEVLE = 9;// 总关卡数

	private int level;// 当前关卡

	private int imagenums;// 图片的数量
	private int totalgametime;// 当前关卡的游戏倒计时的总时间 单位为秒
	private int remembertime;// 记忆时间

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;

		switch (level) {
		case 1:
			imagenums = 3;
			totalgametime = 60;
			remembertime = 5;
			break;
		case 2:
			imagenums = 3;
			totalgametime = 55;
			remembertime = 5;
			break;
		case 3:
			imagenums = 4;
			totalgametime = 50;
			remembertime = 5;
			break;
		case 4:
			imagenums = 6;
			totalgametime = 55;
			remembertime = 5;
			break;
		case 5:
			imagenums = 6;
			totalgametime = 50;
			remembertime = 4;
			break;
		case 6:
			imagenums = 8;
			totalgametime = 45;
			remembertime = 4;
			break;
		case 7:
			imagenums = 10;
			totalgametime = 50;
			remembertime = 4;
			break;
		case 8:
			imagenums = 10;
			totalgametime = 45;
			remembertime = 3;
			break;
		case 9:
			imagenums = 12;
			totalgametime = 40;
			remembertime = 3;
			break;
			

		default:
			break;
		}

	}

	public int getImagenums() {
		return imagenums;

	}

	public int getTotalgametime() {
		return totalgametime;
	}

	public int getRemembertime() {
		return remembertime;
	}

}
