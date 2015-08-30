package com.zj.platform.gamecenter.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.platform.gamecenter.constant.CardTypeEnum;
import com.zj.platform.gamecenter.dto.socket.UserChanceDto;
import com.zj.platform.gamecenter.entity.Card;

@Service
public class CardService {

	@Autowired
	private RandomService randomService;

	private static final Logger logger = LoggerFactory.getLogger(CardService.class);

	public List<Card> upset(List<Card> list) {
		if (CollectionUtils.isEmpty(list)) {
			return list;
		}
		Collections.shuffle(list);
		return list;
	}

	// /**
	// * 每一局的
	// */
	// public Room sendCards(Room room, Bureau bureau) {
	// List<PlayerDto> playerDtoList = bureau.getPlayerList();
	// if (CollectionUtils.isEmpty(playerDtoList)) {
	// logger.info("发牌的时候，房间没有用户存在");
	// } else {
	// HashMap<Long, List<Card>> userCards = gen3Card(bureau);
	//
	// List<PlayerDto> playerDtoList_temp = new ArrayList<PlayerDto>();
	// for (PlayerDto playerDto : playerDtoList) {
	// List<Card> playerCards = userCards.get(playerDto.getUserId());
	// if (CollectionUtils.isNotEmpty(playerCards)) {
	// playerDto.setCards(playerCards);
	// playerDto.setType(PlayGameTypeEnum.SEND_CARDS.getCode());
	// playerDto.setStatus(PlayerStatusEnum.GAMEING.getCode());
	// }
	// playerDtoList_temp.add(playerDto);
	// }
	// room.setPlayerList(playerDtoList_temp);
	// }
	// return room;
	// }

	/**
	 * 生成3张牌 重载方法
	 * 
	 * @param userIds
	 * @param onePairCard
	 * @return
	 */
	public HashMap<Long, List<Card>> gen3Card(List<Long> userIds, List<Card> onePairCard) {
		HashMap<Long, List<Card>> map = new HashMap<Long, List<Card>>();
		int begin = 0;
		int end = 0;
		for (int i = 0; i < userIds.size(); i++) {
			Long userId = userIds.get(i);
			UserChanceDto dto = new UserChanceDto();// 此数据是用户登录玩就进行加载到内存。TODO
			boolean chance = randomService.getUserChance(dto);
			if (chance) {
				List<Integer> cardTypes = new ArrayList<Integer>();// 牌的类型：包子，顺金，金花。。。
				cardTypes.add(20);
				cardTypes.add(16);
				cardTypes.add(12);
				cardTypes.add(8);
				Collections.shuffle(cardTypes);
				map.put(userId, getJinHuaByCardType(cardTypes.get(0), onePairCard));
			} else {
				end = end + 3;
				map.put(userId, onePairCard.subList(begin, end));
				end = end + 1;
				begin = end;
			}
		}
		return map;
	}

	// /**
	// * 生成3张牌
	// *
	// * @param userIds
	// * @return
	// */
	// public HashMap<Long, List<Card>> gen3Card(Bureau bureau) {
	// HashMap<Long, List<Card>> map = new HashMap<Long, List<Card>>();
	// List<Card> onePairCard = bureau.getOnePairCard();
	// List<PlayerDto> playerDtos = bureau.getPlayerList();
	// int begin = 0;
	// int end = 0;
	// for (int i = 0; i < playerDtos.size(); i++) {
	// Long userId = playerDtos.get(i).getUserId();
	// UserChanceDto dto = new UserChanceDto();// 此数据是用户登录玩就进行加载到内存。
	// boolean chance = randomService.getUserChance(dto);
	// if (chance) {
	// List<Integer> cardTypes = new ArrayList<Integer>();// 牌的类型：包子，顺金，金花。。。
	// cardTypes.add(20);
	// cardTypes.add(16);
	// cardTypes.add(12);
	// cardTypes.add(8);
	// Collections.shuffle(cardTypes);
	// map.put(userId, getJinHuaByCardType(cardTypes.get(0), bureau));
	// } else {
	// end = end + 3;
	// map.put(userId, onePairCard.subList(begin, end));
	// end = end + 1;
	// begin = end;
	// }
	// }
	// return map;
	// }

	/**
	 * 根据类型获取牌 重载方法
	 * 
	 * @param cardType
	 * @param cards
	 * @return
	 */
	public List<Card> getJinHuaByCardType(int cardType, List<Card> cards) {
		List<Card> userCards = new ArrayList<Card>();
		if (CardTypeEnum.BAO_ZI.getCode() == cardType) {
			for (int i = 0; i < 14; i++) {//
				Card card = cards.get(i);//

				userCards = hasThree(card, cards);
				if (!CollectionUtils.isEmpty(userCards)) {
					cards.removeAll(userCards);
				}
			}
		} else if (CardTypeEnum.SHUN_JIN.getCode() == cardType) {
			for (int i = 0; i < 14; i++) {
				Card card = cards.get(i);//

				userCards = hasShunJin(card, cards);
				if (!CollectionUtils.isEmpty(userCards)) {
					cards.removeAll(userCards);
				}
			}

		} else if (CardTypeEnum.JIN_HUA.getCode() == cardType) {
			for (int i = 0; i < 14; i++) {
				Card card = cards.get(i);//

				userCards = hasJinHua(card, cards);
				if (!CollectionUtils.isEmpty(userCards)) {
					cards.removeAll(userCards);
				}
			}
		} else if (CardTypeEnum.SHUN_ZI.getCode() == cardType) {
			for (int i = 0; i < 14; i++) {
				Card card = cards.get(i);//

				userCards = hasShunZi(card, cards);
				if (!CollectionUtils.isEmpty(userCards)) {
					cards.removeAll(userCards);
				}
			}
		}
		return userCards;
	}

	// /**
	// * 根据类型获取牌
	// */
	// public List<Card> getJinHuaByCardType(int cardType, Bureau bureau) {
	// List<Card> cards = bureau.getOnePairCard();
	// List<Card> userCards = new ArrayList<Card>();
	// if (CardTypeEnum.BAO_ZI.getCode() == cardType) {
	// for (int i = 0; i < 14; i++) {//
	// Card card = cards.get(i);//
	//
	// userCards = hasThree(card, cards);
	// if (!CollectionUtils.isEmpty(userCards)) {
	// cards.removeAll(userCards);
	// }
	// }
	// } else if (CardTypeEnum.SHUN_JIN.getCode() == cardType) {
	// for (int i = 0; i < 14; i++) {
	// Card card = cards.get(i);//
	//
	// userCards = hasShunJin(card, cards);
	// if (!CollectionUtils.isEmpty(userCards)) {
	// cards.removeAll(userCards);
	// }
	// }
	//
	// } else if (CardTypeEnum.JIN_HUA.getCode() == cardType) {
	// for (int i = 0; i < 14; i++) {
	// Card card = cards.get(i);//
	//
	// userCards = hasJinHua(card, cards);
	// if (!CollectionUtils.isEmpty(userCards)) {
	// cards.removeAll(userCards);
	// }
	// }
	// } else if (CardTypeEnum.SHUN_ZI.getCode() == cardType) {
	// for (int i = 0; i < 14; i++) {
	// Card card = cards.get(i);//
	//
	// userCards = hasShunZi(card, cards);
	// if (!CollectionUtils.isEmpty(userCards)) {
	// cards.removeAll(userCards);
	// }
	// }
	// }
	// bureau.setOnePairCard(cards);
	// return userCards;
	// }

	private List<Card> hasThree(Card card, List<Card> cards) {
		List<Card> userCards = new ArrayList<Card>();
		int colors[] = { 1, 2, 3, 4 };// 黑，红，梅，方
		for (int c : colors) {
			Card other_card = new Card(card.getNum(), c);
			if (cards.contains(other_card)) {
				userCards.add(other_card);
			}
		}

		if (!CollectionUtils.isEmpty(userCards)) {
			if (userCards.size() > 2) {
				if (userCards.size() == 4) {
					userCards.remove(3);
				}
				return userCards;
			}
			return null;
		}
		return null;
	}

	public List<Card> hasShunJin(Card card, List<Card> cards) {
		List<Card> userCards = new ArrayList<Card>();

		int num = card.getNum();
		if (num > 12) {
			Card pre_1 = new Card(num--, card.getColor());
			Card pre_2 = new Card(num--, card.getColor());

			if (cards.contains(pre_1) && cards.contains(pre_2)) {
				userCards.add(pre_1);
				userCards.add(pre_2);
				return userCards;
			} else {
				return null;
			}
		} else {
			Card next_1 = new Card(num++, card.getColor());
			Card next_2 = new Card(num++, card.getColor());

			if (cards.contains(next_1) && cards.contains(next_2)) {
				userCards.add(next_1);
				userCards.add(next_2);
				return userCards;
			} else {
				Card pre_1 = new Card(num--, card.getColor());
				Card pre_2 = new Card(num--, card.getColor());

				if (cards.contains(pre_1) && cards.contains(pre_2)) {
					userCards.add(pre_1);
					userCards.add(pre_2);
					return userCards;
				} else {
					return null;
				}
			}
		}
	}

	public List<Card> hasJinHua(Card card, List<Card> cards) {
		List<Card> userCards = new ArrayList<Card>();

		int i = 0;
		for (Card _card : cards) {
			if (_card.getColor() == card.getColor()) {
				i++;
				userCards.add(_card);
				if (i > 3) {
					break;
				}
			}
		}
		return userCards;
	}

	public List<Card> hasShunZi(Card card, List<Card> cards) {
		List<Card> userCards = new ArrayList<Card>();
		List<Integer> colors = new ArrayList<Integer>();
		colors.add(1);
		colors.add(2);
		colors.add(3);
		colors.add(4);
		Collections.shuffle(colors);
		int num = card.getNum();
		if (num > 12) {
			Card pre_1 = new Card(num--, colors.get(0));
			Card pre_2 = new Card(num--, colors.get(1));

			if (cards.contains(pre_1) && cards.contains(pre_2)) {
				userCards.add(pre_1);
				userCards.add(pre_2);
				return userCards;
			} else {
				return null;
			}
		} else {
			Card next_1 = new Card(num++, colors.get(0));
			Card next_2 = new Card(num++, colors.get(1));

			if (cards.contains(next_1) && cards.contains(next_2)) {
				userCards.add(next_1);
				userCards.add(next_2);
				return userCards;
			} else {
				Card pre_1 = new Card(num--, colors.get(0));
				Card pre_2 = new Card(num--, colors.get(2));

				if (cards.contains(pre_1) && cards.contains(pre_2)) {
					userCards.add(pre_1);
					userCards.add(pre_2);
					return userCards;
				} else {
					return null;
				}
			}
		}
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
		List<Long> userIds = new ArrayList<Long>();
		userIds.add(1L);
		userIds.add(2L);

		// CardService ser = new CardService();
		// Map<Long, List<int[]>> map = ser.gen3Card(bureau)(userIds);
		// List<int[]> one = map.get(1L);
		// List<int[]> two = map.get(2L);
		//
		// System.out.println(CardUtils.gen(one.get(0)) + "," +
		// CardUtils.gen(one.get(1)) + "," + CardUtils.gen(one.get(2)));
		//
		// System.out.println(CardUtils.gen(two.get(0)) + "," +
		// CardUtils.gen(two.get(1)) + "," + CardUtils.gen(two.get(2)));
		//
		// System.out.println(_isShunZi(toUserCard));
		//
		// System.out.println(_shunZiCompare(newUserCard, newToUserCard));

		// int result = CardUtils.compareCard(one, two);
		int result = 0;
		if (result == 1) {
			System.out.println("大于");
		} else if (result == 0) {
			System.out.println("小于");
		} else if (result == 2) {
			System.out.println("等于");
		}
	}
}
