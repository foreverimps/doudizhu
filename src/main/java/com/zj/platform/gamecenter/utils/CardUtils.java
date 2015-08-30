package com.zj.platform.gamecenter.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zj.platform.gamecenter.constant.CardTypeEnum;
import com.zj.platform.gamecenter.entity.Card;

public class CardUtils {

	public static final int[] SIGLE_CARD = new int[] { 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 201, 202, 203, 204, 205, 206,
	        207, 208, 209, 210, 211, 212, 213, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 401, 402, 403, 404, 405, 406, 407,
	        408, 409, 410, 411, 412, 413 };

	public static final int[] SIGLE_CARD_WITH_GHOST = new int[] {

	};

	/** 豹子 */
	public static boolean _is3tiao(List<Card> card) {
		int a = card.get(0).getNum();// 牌的数值
		int b = card.get(1).getNum();
		int c = card.get(2).getNum();
		if (a == b && b == c) {
			return true;
		}
		return false;
	}

	/** 金花 */
	public static boolean _isJinhua(List<Card> card) {
		int a = card.get(0).getColor();// 牌的花色
		int b = card.get(1).getColor();
		int c = card.get(2).getColor();
		if (a == b && b == c) {
			return true;
		}
		return false;
	}

	/** * 顺子 */
	public static boolean _isShunZi(List<Card> card) {
		int a = card.get(0).getColor();// 牌的数值
		int b = card.get(1).getColor();
		int c = card.get(2).getColor();

		int[] newCard = { a, b, c };
		Arrays.sort(newCard); // 进行排序

		if (newCard[2] - 1 == newCard[1] && newCard[1] - 1 == newCard[0]) {
			return true;
		} else {
			if (a == 14) {
				a = 1;
			}
			if (b == 14) {
				b = 1;
			}
			if (c == 14) {
				c = 1;
			}

			int[] newCard2 = { a, b, c };
			Arrays.sort(newCard2); // 进行排序
			if (newCard2[2] - 1 == newCard2[1] && newCard2[1] - 1 == newCard2[0]) {
				return true;
			}
			return false;
		}
	}

	/** 对子 */
	public static boolean _isDouble(List<Card> card) {
		int a = card.get(0).getNum();// 牌的数值
		int b = card.get(1).getNum();
		int c = card.get(2).getNum();
		if (a == b || b == c || a == c) {
			return true;
		}
		return false;
	}

	/** 获取牌大小的类型 */
	public static int getCardCompareType(List<Card> card) {
		if (_is3tiao(card)) {
			return CardTypeEnum.BAO_ZI.getCode();
		} else {
			if (_isJinhua(card)) {
				if (_isShunZi(card)) {
					return CardTypeEnum.SHUN_JIN.getCode();
				}
				return CardTypeEnum.JIN_HUA.getCode();
			} else {
				if (_isShunZi(card)) {
					return CardTypeEnum.SHUN_ZI.getCode();
				} else {
					if (_isDouble(card)) {
						return CardTypeEnum.DOUBLE.getCode();
					}
				}
			}
		}
		return CardTypeEnum.SINGLE.getCode();
	}

	public static int _baoZiCompare(int[] newUserCard, int[] newToUserCard) {
		int e = newUserCard[0];
		int e2 = newToUserCard[0];
		int result = 1;
		if (e > e2) {
			result = 1;
		}
		if (e < e2) {
			result = 0;
		}
		return result;
	}

	public static int _shunJinCompare(int[] newUserCard, int[] newToUserCard) {
		int _card3 = newUserCard[2];
		int _2card3 = newToUserCard[2];
		if (newUserCard[2] == 14) {// 是否有A
			if (newUserCard[0] == 2) {
				int a = 1;
				int b = newUserCard[0];
				int c = newUserCard[1];
				int[] _newUserCard = { a, b, c };
				newUserCard = _newUserCard;
			}
		}

		if (newToUserCard[2] == 14) {
			if (newToUserCard[0] == 2) {// 是否有A
				int a = 1;
				int b = newToUserCard[0];
				int c = newToUserCard[1];
				int[] _newToUserCard = { a, b, c };
				newToUserCard = _newToUserCard;
			}
		}

		_card3 = newUserCard[2];
		_2card3 = newToUserCard[2];

		int result = 2;
		if (_card3 > _2card3) {
			result = 1;
		}
		if (_card3 < _2card3) {
			result = 0;
		}
		if (_card3 == _2card3) {
			result = 2;
		}
		return result;
	}

	public static int _jinHuaCompare(int[] newUserCard, int[] newToUserCard) {
		int result = 2;// 相等
		int _card3 = newUserCard[2];
		int _2card3 = newToUserCard[2];

		if (_card3 > _2card3) {
			result = 1;// true;
		}
		if (_card3 < _2card3) {
			result = 0;
		}
		return result;
	}

	public static int _shunZiCompare(int[] newUserCard, int[] newToUserCard) {
		int _card3 = newUserCard[2];
		int _2card3 = newToUserCard[2];
		if (newUserCard[2] == 14) {// 是否有A
			if (newUserCard[0] == 2) {
				int a = 1;
				int b = newUserCard[0];
				int c = newUserCard[1];
				int[] _newUserCard = { a, b, c };
				newUserCard = _newUserCard;
			}
		}

		if (newToUserCard[2] == 14) {
			if (newToUserCard[0] == 2) {// 是否有A
				int a = 1;
				int b = newToUserCard[0];
				int c = newToUserCard[1];
				int[] _newToUserCard = { a, b, c };
				newToUserCard = _newToUserCard;
			}
		}

		_card3 = newUserCard[2];
		_2card3 = newToUserCard[2];

		int result = 2;
		if (_card3 > _2card3) {
			result = 1;
		}
		if (_card3 < _2card3) {
			result = 0;
		}
		if (_card3 == _2card3) {
			result = 2;
		}
		return result;
	}

	public static int _doubleCompare(int[] newUserCard, int[] newToUserCard) {
		int _card2 = newUserCard[1];// 因为是排序过的，所以中间的一张肯定是
		                            // 对子中的一张，也就是我只需要比较中间一张的大小就ok了

		int _2card2 = newToUserCard[1];
		int result = 2;// 相等
		if (_card2 > _2card2) {
			result = 1;// true;
		}
		if (_card2 < _2card2) {
			result = 0;
		}
		if (_card2 == _2card2) {
			int defCard = newUserCard[0];// 单张和对子大小的比较，如果是大，那么单张就是第一个，如果是小，单张就是第三个，我现在并不知道大小，所以我先获取第一个

			if (defCard == _card2) {// 如果第一个跟中间的相等，就说明，他是对子中的一个，那么单张的就是第三个
				defCard = newUserCard[2];
			}
			int defCard2 = newToUserCard[0];
			if (defCard2 == _2card2) {
				defCard2 = newToUserCard[2];
			}
			if (defCard > defCard2) {
				result = 1;// true;
			}
			if (defCard < defCard2) {
				result = 0;
			}
		}
		return result;
	}

	public static int _singleCompare(int[] newUserCard, int[] newToUserCard) {
		int _card3 = newUserCard[2];
		int _2card3 = newToUserCard[2];
		int result = 2;// 相等
		if (_card3 > _2card3) {
			result = 1;// true;
		}
		if (_card3 < _2card3) {
			result = 0;
		}
		return result;
	}

	public static int compareCard(List<Card> userCard, List<Card> toUserCard) {
		int userCardType = getCardCompareType(userCard);
		int toUserCardType = getCardCompareType(toUserCard);
		if (userCardType > toUserCardType) {
			return 1;
		}
		if (userCardType < toUserCardType) {
			return 0;
		}
		if (userCardType == toUserCardType) {
			int a = userCard.get(0).getNum();// 牌的数值
			int b = userCard.get(1).getNum();
			int c = userCard.get(2).getNum();

			int[] newUserCard = { a, b, c };
			Arrays.sort(newUserCard); // 进行排序

			int a2 = toUserCard.get(0).getNum();// 牌的数值
			int b2 = toUserCard.get(1).getNum();
			int c2 = toUserCard.get(2).getNum();

			int[] newToUserCard = { a2, b2, c2 };
			Arrays.sort(newToUserCard); // 进行排序

			if (CardTypeEnum.BAO_ZI.getCode() == userCardType) {
				return _baoZiCompare(newUserCard, newToUserCard);

			} else if (CardTypeEnum.SHUN_JIN.getCode() == userCardType) {
				return _shunJinCompare(newUserCard, newToUserCard);

			} else if (CardTypeEnum.JIN_HUA.getCode() == userCardType) {
				return _jinHuaCompare(newUserCard, newToUserCard);

			} else if (CardTypeEnum.SHUN_ZI.getCode() == userCardType) {
				return _shunZiCompare(newUserCard, newToUserCard);

			} else if (CardTypeEnum.DOUBLE.getCode() == userCardType) {
				return _doubleCompare(newUserCard, newToUserCard);

			} else if (CardTypeEnum.SINGLE.getCode() == userCardType) {
				return _singleCompare(newUserCard, newToUserCard);

			}
		}
		return 0;
	}

	/**
	 * main函数.
	 * 
	 * @param args
	 *            启动参数
	 * @throws Exception
	 *             Exception
	 */
	public static void main(String... args) throws Exception {
		List<Card> userCard = new ArrayList<Card>();
		List<Card> toUserCard = new ArrayList<Card>();
		Card a = new Card(8, 1);
		Card b = new Card(8, 3);
		Card c = new Card(8, 1);
		userCard.add(a);
		userCard.add(b);
		userCard.add(c);

		Card a2 = new Card(9, 1);
		Card b2 = new Card(9, 1);
		Card c2 = new Card(10, 2);

		toUserCard.add(a2);
		toUserCard.add(b2);
		toUserCard.add(c2);

		System.out.println(gen(a) + "," + gen(b) + "," + gen(c));

		System.out.println(gen(a2) + "," + gen(b2) + "," + gen(c2));
		//
		// System.out.println(_isShunZi(toUserCard));
		//
		// System.out.println(_shunZiCompare(newUserCard, newToUserCard));

		int result = compareCard(userCard, toUserCard);
		if (result == 1) {
			System.out.println("大于");
		} else if (result == 0) {
			System.out.println("小于");
		} else if (result == 2) {
			System.out.println("等于");
		}
	}

	public static String gen(Card a) {
		String type = "";
		if (a.getColor() == 1) {
			type = "黑桃";
		} else if (a.getColor() == 2) {
			type = "红桃";
		} else if (a.getColor() == 3) {
			type = "梅花";
		} else if (a.getColor() == 4) {
			type = "方块";
		}
		return "" + a.getNum() + type;
	}
}
