package lottoTeam3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LottoRecord {
	private final int lottoRound;
	private List<Integer> lotteryNums; // 당첨 번호
	private int lotteryBonus; // 당첨 보너스 번호
	private List<LottoData[]> buyLotto; // 구매자가 선택한 번호
	private List<Integer[]> rankList;

	public int getLottoRound() {
		return lottoRound;
	}

	public List<Integer> getLotteryNums() {
		return lotteryNums;
	}

	public int getLotteryBonus() {
		return lotteryBonus;
	}

	public List<LottoData[]> getBuyLotto() {
		return buyLotto;
	}

	public LottoRecord(int lottoRound) {
		this.lottoRound = lottoRound;
		buyLotto = new ArrayList<>();
	}

	public boolean hasWin() {
		for (int i = 0; i < rankList.size(); i++) {
			if (isWin(i))
				return true;
		}
		return false;
	}

	public boolean isWin(int beon) {
		Integer[] rank = rankList.get(beon);
		for (int i = 0; i < rank.length; i++) {
			if (rank[i] == null)
				break;
			if (rank[i] < 6)
				return true;
		}
		return false;
	}

	public void SetLottery(List<Integer> lotteryNums, int lotteryBonus) {
		this.lotteryNums = lotteryNums;
		this.lotteryBonus = lotteryBonus;
		rankList = new ArrayList<>();
		for (int i = 0; i < buyLotto.size(); i++) {
			LottoData[] lottoDatas = buyLotto.get(i);
			Integer[] ranks = new Integer[5];
			for (int j = 0; j < 5; j++) {
				if (lottoDatas[j] != null) {
					int[] lottoArr = lottoDatas[j].getNums();
					int count = 0;
					for (int k = 0; k < lottoArr.length; k++) {
						if (lotteryNums.contains(lottoArr[k])) {
							count++;
						}
					}
					int rank;
					// 1등, 6개 번호 일치
					if (count == 6) {
						rank = 1;
					} else if (count == 5) {
						// 3등, 5개 번호 일치
						rank = 3;

						// 2등, 5개 번호 일치 + 보너스 볼과 번호 일치
						for (int k = 0; k < lottoArr.length; k++) {
							if (lottoArr[k] == lotteryBonus) {
								rank = 2;
								break;
							}
						}
						// 4등, 4개 번호 일치
					} else if (count == 4) {
						rank = 4;
						// 5등, 3개 번호 일치
					} else if (count == 3) {
						rank = 5;
					} else {
						rank = 6;
					}
					ranks[j] = rank;
				}
			}
			rankList.add(ranks);
		}
//		for (int i = 0; i < rankList.size(); i++) {
//			Integer[] ranks = rankList.get(i);
//			System.out.print(Arrays.toString(ranks) + " ");
//		}
//		System.out.println();
	}

	public Integer getLottoRank(int beon, int game) {
		return rankList.get(beon)[game];
	}

	public long getPrize(int beon) {
		if (beon < 0)
			return 0;
		long result = 0;
		Integer[] ranks = rankList.get(beon);
		for (int i = 0; i < 5; i++) {
			if (ranks[i] == null)
				break;
			result += rankToPrize(ranks[i]);
		}
		return result;
	}

	public void addBuyLotto(LottoData[] lottoDatas) {
		buyLotto.add(lottoDatas);
	}

	public int getProceeds() {
		int result = 0;
		for (Integer[] ranks : rankList) {
			for (Integer rank : ranks) {
				if (rank == null)
					break;
				result += rankToPrize(rank);
			}
		}
		return result;
	}

	public int getInvestment() {
		int result = 0;
		for (LottoData[] lottoDatas : buyLotto) {
			for (LottoData lottoData : lottoDatas) {
				if (lottoData == null)
					break;
				result += 1000;
			}
		}
		return result;
	}

	private long rankToPrize(int rank) {
		return rank == 1 ? 1_952_160_000
				: rank == 2 ? 54_226_666 : rank == 3 ? 1_427_017 : rank == 4 ? 50_000 : rank == 5 ? 5_000 : 0;
	}

	public static LottoRecord tryParse(String line) {
		String[] splitFields = line.split("/");
		LottoRecord lottoRecord = new LottoRecord(Integer.parseInt(splitFields[0]));
		String[] strNums = splitFields[1].substring(1, splitFields[1].length() - 1).split(", ");
		Integer[] iNums = new Integer[6];
		for (int i = 0; i < iNums.length; i++) {
			iNums[i] = Integer.valueOf(strNums[i]);
		}
		if (splitFields.length < 4) {
			lottoRecord.SetLottery(Arrays.asList(iNums), Integer.parseInt(splitFields[2]));
			return lottoRecord;
		}

		String[] splitBuy = splitFields[3].split("\\+");

		for (int i = 0; i < splitBuy.length; i++) {
			LottoData[] lottoDatas = new LottoData[5];
			String[] splitGame = splitBuy[i].substring(1, splitBuy[i].length() - 1).split(", ");
			for (int j = 0; j < splitGame.length; j++) {
				if (splitGame[j].equals("null")) {
					lottoDatas[j] = null;
					continue;
				}
				String[] splitLottoData = splitGame[j].split(" ");
				int[] nums = new int[6];
				for (int k = 0; k < splitLottoData.length - 1; k++) {
					nums[k] = Integer.parseInt(splitLottoData[k]);
				}
				Mode mode = Mode.getMode(splitLottoData[6]);
				lottoDatas[j] = new LottoData(nums, mode);
			}
			lottoRecord.buyLotto.add(lottoDatas);
		}
		lottoRecord.SetLottery(Arrays.asList(iNums), Integer.parseInt(splitFields[2]));
		return lottoRecord;
	}

	public LottoData[] getLottoDatas(int index) {
		if (index < buyLotto.size())
			return buyLotto.get(index);
		return null;
	}

	public boolean hasBought() {
		return !buyLotto.isEmpty();
	}

	public int getPuchaseNum() {
		return buyLotto.size();
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(lottoRound).append('/').append(lotteryNums).append('/').append(lotteryBonus).append('/');
		for (LottoData[] lottoDatas : buyLotto) {
			stringBuilder.append(Arrays.toString(lottoDatas)).append('+');
		}
		return stringBuilder.toString();
	}

//	public static void main(String[] args) {
//		String s = "1/[1, 2, 3, 4, 5, 6]/0/[8 12 15 24 29 35 AUTO, 16 20 21 27 30 40 AUTO, 1 7 26 33 38 41 AUTO, 5 7 10 16 21 43 AUTO, null]+[6 14 28 29 39 43 AUTO, 4 9 10 19 24 43 AUTO, 9 19 22 23 31 41 AUTO, 13 17 18 21 35 42 AUTO, 3 7 11 15 25 37 AUTO]+[1 8 12 25 43 45 AUTO, 4 9 19 24 29 39 AUTO, 8 14 19 26 37 42 AUTO, 17 25 28 31 34 38 AUTO, 12 29 31 33 37 42 AUTO]+";
//		LottoRecord lr = LottoRecord.tryParse(s);
//		System.out.println(lr.getLottoDatas(0));
//		System.out.println(s.equals(lr.toString()));
//	}

}
