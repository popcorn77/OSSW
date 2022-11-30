package normalclass;


import java.util.ArrayList;
import java.util.Arrays;


/** AnalyzerPredictEnemy - 상대 패를 분석하여 나올 수 있는 모든 경우 예측 */
public class AnalyzerPredictEnemy extends AnalyzerBasic {

	private int reqSize;
	private int denom;
	private int[] usableCardNum = new int[13];
	private int[] usableCardShape = new int[4];
	private float[] predictArr = new float[9];
	private User user;
	
	/** 상대패 예측 분석*/
	public float[] analysis(ArrayList<Card> passDeck, User user, String target, int unknown) {
		System.out.println("player"+user.getUserName()+"의 "+target+" 분석표");
		super.analysis(passDeck);
		this.reqSize = passDeck.size();
		this.user = user;
		this.initA();
		
		/*usableCardNum[0] = 1;
		usableCardNum[1] = 2;
		usableCardNum[2] = 2;
		usableCardNum[3] = 1;
		usableCardNum[4] = 2;
		usableCardNum[5] = 3;
		usableCardNum[6] = 3;
		usableCardNum[7] = 2;
		usableCardNum[8] = 2;
		usableCardNum[9] = 3;
		usableCardNum[12] = 3;
		usableCardShape[1] = 9;*/
		
		for(int q = 0 ; q < user.getCardMemory().size() ; q++) {
			usableCardNum[user.getCardMemory().get(q).getCNum() - 2] --;
			usableCardShape[user.getCardMemory().get(q).getCShape() - 1] --;
			denom --;
		}
		
		loop : for(int q = user.getGrade(0) ; q < predictArr.length ; q++) {//자신의 등급에 따라 확률을 구함 
			float result = 0f;
			switch(q) {
			case 0://one pair
				result = caseOnePair(unknown, usableCardNum);
				result = resetResult(q, result);
				break;

			case 1://two pair
				switch(getTmpGrade(0)) {
				case 0:
					result = caseTwoPair1(unknown, usableCardNum);
					break;
				case 1:
					result = caseTwoPair2(unknown, usableCardNum);
					break;
				}
				result = resetResult(q, result);
				break;

			case 2://triple
				switch(getTmpGrade(0)) {
				case 0:
					result = caseTriple1(unknown, usableCardNum);
					break;
				case 1:case 2:
					result = caseTriple2(unknown, usableCardNum);
					break;
				}
				result = resetResult(q, result);
				break;

			case 3://straight
				result = caseStraight(unknown, usableCardNum);
				result = resetResult(q, result);
				break;

			case 4://flush
				result = caseFlush(unknown, usableCardShape);
				result = resetResult(q, result);
				break;

			case 5://full house
				switch(getTmpGrade(0)) {
				case 0:
					result = caseFullHouse1(unknown, usableCardNum);
					break;
				case 1:
					result = caseFullHouse2(unknown, usableCardNum);
					break;
				case 2:
					result = caseFullHouse3(unknown, usableCardNum);
					break;
				case 3:
					result = caseFullHouse4(unknown, usableCardNum);
					break;
				}
				result = resetResult(q, result);
				break;

			case 6://four card
				switch(getTmpGrade(0)){
				case 0:
					result = caseFourCard1(unknown, usableCardNum);
					break;
				case 1:
					result = caseFourCard2(unknown, usableCardNum);
					break;
				case 2:
					result = caseFourCard3(usableCardNum);
					break;
				case 3:
					result = caseFourCard4(unknown, usableCardNum);
					break;
				}
				result = resetResult(q, result);
				break;
				
			case 7://요청패의 등급이 포카드 일때는 분석할 필요 없음
				if(getTmpGrade(0) == 7) break loop;
				else break;
				
			case 8://straight flush, Royal straight flush
				result = caseStraightFlush(unknown, usableCardNum);
				result = resetResult(7, result);
				
				if(predictArr[3] != 0) predictArr[3] -= predictArr[7] + predictArr[8];
				if(predictArr[4] != 0) predictArr[4] -= predictArr[7] + predictArr[8];
				break;
			}//switch
		}
		System.out.println("분석 결과 ->"+Arrays.toString(predictArr)+"\n");
		super.initA();
		return predictArr;
	}

	private int resetResult(int index, float result) {
		result = Math.round(result * 1000000) / 1000000f;
		predictArr[index] = result;
		return 0;
	}

	private float formulaA(float molec, int denom) {//OO
		return molec / denom * (molec - 1) / (denom - 1);
	}
	
	private float formulaB(float molec, int denom) {//OX
		return molec / denom * (denom - molec) / (denom - 1);
	}
	
	private float formulaC(float molec, float molec2, int denom) {//??
		return molec / denom * molec2 / (denom - 1);
	}
	
	private float FormulaD(int notOwnCard, float molec, int denom, int[] usableCardNum) {//2번째가 제외
		float sum = 0f, molec2 = 0;

		for(int q = 1 ; q < numberDeck.length ; q++)
			if(numberDeck[q] == 0)
			{
				molec2 = usableCardNum[q - 1];
				sum += (formulaC(molec, molec2, denom) * (notOwnCard - molec2) / (denom - 2)) * 6;
			}
		return sum;
	}

	private int exceptionCard(int[] ubCN){
		int except = 0;
		for(int q = 1 ; q < numberDeck.length ; q++)
			if(numberDeck[q] == 0)
				except += ubCN[q - 1];

		return except;
	}//미 보유카드 합계

	private float caseFlush(int unknown, int[] ubCS) {
		// denominator - > 분모 / molecule -> 분자 / ubCS ->사용가능한 모양 배열
		
		float sum = 0f, molec = 0f;

		for(int q = 0 ; q < shapeDeck.length ; q++) {
			molec = ubCS[q];
			switch(shapeDeck[q]) {
			case 2:
				switch(unknown) {
				case 3:
					sum += formulaA(molec, denom) * ((molec - 2) / (denom - 2));
					break;
				}
				break;
			case 3:
				switch(unknown) {
				case 2:
					sum += formulaA(molec, denom);
					break;
				case 3:
					sum += (formulaA(molec, denom) * (denom - molec) / (denom - 2)) * 3;
					break;
				}
				break;
			case 4:
				switch(unknown) {
				case 2:
					sum += formulaB(molec, denom) * 2;
					break;
				case 3:
					sum += (formulaB(molec, denom) * ((denom - molec) - 1) /(denom - 2)) * 3;
					break;
				}
				break;
			}
		}
		return sum * 100;
	}

	private float caseStraight(int unknown, int[] ubCN) {
		// denominator - > 분모 / molecule -> 분자 / ubCN ->사용가능한 카드 배열
		
		ArrayList<int[][]> stGroup = getStGroup();
		ArrayList<Float> list = new ArrayList<Float>();
		float sum = 0f;

		System.out.println("<가능한 스트레이트 목록> ─── <필요한 카드(0)>");
		for(int q = 0 ; q < stGroup.size() ; q++)
			System.out.println(Arrays.deepToString(stGroup.get(q)));

		for(int q = 0 ; q < stGroup.size() ; q++)
		{
			int except = getExcept(stGroup.get(q), ubCN, 0);
			int count = 0;
			int tmp = 0;

			for(int w = 0 ; w < stGroup.get(q)[1].length ; w++)
				if(stGroup.get(q)[1][w] == 1)
					count ++;
				else
					list.add((float)ubCN[stGroup.get(q)[0][w] - 2]);

			switch(count) {//cnt - 완성을 위해 현재 보유중인 카드 수
			case 2:
				switch(unknown) {
				case 3:
					sum += (formulaC(list.get(0), list.get(1), denom) * list.get(2) / (denom - 2)) * 6;
					break;
				}
				break;
			case 3:
				switch(unknown) {
				case 2:
					sum += formulaC(list.get(0), list.get(1), denom) * 2;
					break;
				case 3:
					tmp = denom - (int)(list.get(0) + list.get(1) + except);
					sum += (formulaC(list.get(0), list.get(1), denom) * tmp / (denom - 2)) * 6;
					break;
				}
				break;
			case 4:
				switch(unknown) {
				case 2:
					tmp = denom - (int)(except + list.get(0));
					sum += formulaC(list.get(0), tmp, denom) * 2;
					break;
				case 3:
					tmp = denom - (int)(except + list.get(0));
					sum += (list.get(0) / denom * formulaA(tmp, denom - 1)) * 3;
					break;
				}
				break;
			}
			list.clear();
		}
		return sum * 100;
	}

	private float caseOnePair(int unknown, int[] ubCN) {
		// denominator - > 분모 / molecule -> 분자 / ubCN ->사용가능한 카드 배열

		float sum = 0f, molec = 0;

		if(getTmpGrade(0) < 1)
		{
			for(int q = 1 ; q < numberDeck.length ; q++)
				if(numberDeck[q] == 0)
				{
					molec = ubCN[q - 1];
					switch(unknown){
					case 2:
						sum += formulaA(molec, denom);
						break;
					case 3:
						sum += (formulaA(molec, denom) * (exceptionCard(ubCN) - (molec - 2)) / (denom - 2)) * 3;
						break;
					}
				}

			for(int q = 0 ; q < reqSize ; q++)
			{
				molec = ubCN[requestDeck.get(q).getCNum() - 2];
				switch(unknown) {
				case 2:
					sum += formulaC(molec, exceptionCard(ubCN), denom) * 2;
					break;
				case 3:
					sum += FormulaD(exceptionCard(ubCN), molec, denom, ubCN);
					break;
				}
			}
		}
		return sum * 100;
	}

	private float caseTwoPair1(int unknown, int[] ubCN) {
		// denominator - > 분모 / molecule -> 분자 / ubCN ->사용가능한 카드 배열

		float sum = 0f, molec = 0, molec2 = 0;

		for(int q = 0 ; q < reqSize ; q++)
		{
			molec2 = 0;
			molec = ubCN[requestDeck.get(q).getCNum() - 2];

			for(int w = 0 ; w < reqSize ; w++)
				if(q != w)
					molec2 += ubCN[requestDeck.get(w).getCNum() - 2];

			switch(unknown) {
			case 2:
				sum += formulaC(molec, molec2, denom) * 2;
				break;
			case 3:
				sum += (formulaC(molec, molec2, denom) * exceptionCard(ubCN) / (denom - 2)) * 6;
				break;
			}
		}
		return sum * 100;
	}

	private float caseTwoPair2(int unknown, int[] ubCN) {
		// denominator - > 분모 / molecule -> 분자 / ubCN ->사용가능한 카드 배열

		float sum = 0f, molec = 0;

		for(int q = 1 ; q < numberDeck.length ; q++)
		{
			if(numberDeck[q] == 0)
			{
				molec = ubCN[q - 1];
				switch(unknown) {
				case 2:
					sum += formulaA(molec, denom);
					break;
				case 3:
					sum += (formulaA(molec, denom) * (exceptionCard(ubCN) - molec) / (denom - 2)) * 3;
					break;
				}
			}
		}

		for(int q = 0 ; q < reqSize ; q++)
		{
			if(numberDeck[requestDeck.get(q).getCNum() - 1] == 1)
			{
				molec = ubCN[requestDeck.get(q).getCNum() - 2];
				switch(unknown) {
				case 2:
					sum += (formulaC(molec, exceptionCard(ubCN), denom)) * 2;
					break;
				case 3:
					sum += (molec / denom * formulaA(exceptionCard(ubCN), denom - 2)) * 3 ;
					break;
				}
			}
		}
		return sum * 100;
	}

	private float caseTriple1(int unknown, int[] ubCN) {
		// denominator - > 분모 / molecule -> 분자 / ubCN ->사용가능한 카드 배열

		float sum = 0f, molec = 0;

		for(int q = 1 ; q < numberDeck.length ; q++)
			if(numberDeck[q] == 0 && unknown == 3)
			{
				molec = ubCN[q - 1];
				sum += formulaA(molec, denom) * (molec - 2) / (denom - 2);
			}

		for(int q = 0 ; q < reqSize ; q++)
		{
			molec = ubCN[requestDeck.get(q).getCNum() - 2];
			switch(unknown) {
			case 2:
				sum += formulaA(molec, denom);
				break;
			case 3:
				sum += (formulaA(molec, denom) * exceptionCard(ubCN) / (denom - 2)) * 3;
				break;
			}
		}
		return sum * 100;
	}

	private float caseTriple2(int unknown, int[] ubCN) {
		// denominator - > 분모 / molecule -> 분자 / ubCN ->사용가능한 카드 배열

		float sum = 0f, molec = 0;

		for(int q = 0 ; q < reqSize ; q++)
			if(numberDeck[requestDeck.get(q).getCNum() - 1] == 2)
			{
				molec = ubCN[requestDeck.get(q).getCNum() - 2];
				switch(unknown) {
				case 2:
					sum += formulaC(molec, exceptionCard(ubCN), denom) * 2;
					break;
				case 3:
					sum += FormulaD(exceptionCard(ubCN), molec, denom, ubCN);
					break;
				}
				break;
			}
		return sum * 100;
	}

	private float caseFullHouse1(int unknown, int[] ubCN) {
		// denominator - > 분모 / molecule -> 분자 / ubCN ->사용가능한 카드 배열

		float sum = 0f, molec = 0, molec2 = 0;

		if(unknown == 3)
			for(int q = 0 ; q < reqSize ;  q++)
			{
				for(int w = 0 ; w < reqSize ; w++)
					if(q != w)
						molec2 += ubCN[requestDeck.get(w).getCNum() - 2];

				molec = ubCN[requestDeck.get(q).getCNum() - 2];
				sum += (formulaA(molec,denom) * molec2 / (denom - 2)) * 3;
			}

		return sum * 100;
	}

	private float caseFullHouse2(int unknown, int[] ubCN) {
		// denominator - > 분모 / molecule -> 분자 / ubCN ->사용가능한 카드 배열

		float sum = 0f, molec = 0, molec2 = 0;
		boolean flag = true;

		if(unknown == 3)
			for(int q = 1 ; q < numberDeck.length ; q++)
				if(numberDeck[q] == 0)
				{
					molec = ubCN[q - 1];
					sum += formulaA(molec, denom) * (molec - 2) / (denom - 2);
				}

		for(int q = 0 ; q < reqSize ;  q++)
		{
			molec = ubCN[requestDeck.get(q).getCNum() - 2];

			if(numberDeck[requestDeck.get(q).getCNum() - 1] == 2 && flag)
			{
				flag = false;
				for(int w = 0 ; w < reqSize ; w++)
					if(q != w && numberDeck[requestDeck.get(w).getCNum() - 1] == 1)
						molec2 += ubCN[requestDeck.get(w).getCNum() - 2];

				switch(unknown) {
				case 2:
					sum += formulaC(molec, molec2, denom) * 2;
					break;
				case 3:
					sum += (formulaC(molec, molec2, denom) * exceptionCard(ubCN) / (denom - 2)) * 6;
					break;
				}
			}
			else if(numberDeck[requestDeck.get(q).getCNum() - 1] == 1 && unknown == 3)
				sum += (formulaA(molec, denom) * exceptionCard(ubCN) / (denom - 2)) * 3;
		}

		return sum * 100;
	}


	private float caseFullHouse3(int unknown, int[] ubCN) {
		// denominator - > 분모 / molecule -> 분자 / ubCN ->사용가능한 카드 배열

		ArrayList<Integer> doubleList = getDoubleList();
		float sum = 0f, molec = 0;
		
		if(unknown == 3)
			for(int q = 1 ; q < numberDeck.length ; q++)
				if(numberDeck[q] == 0)
				{
					molec = ubCN[q - 1];
					sum += formulaA(molec, denom) * (molec - 2) / (denom - 2);
				}

		for(int q = 0 ; q < doubleList.size() ; q++)
		{
			molec = ubCN[doubleList.get(q) - 2];
			switch(unknown) {
			case 2:
				sum += (molec / denom * exceptionCard(ubCN) / (denom - 1) ) * 2;
				break;
			case 3:
				sum += (molec / denom * formulaA(exceptionCard(ubCN), denom - 2)) * 3;
				break;
			}
		}
		return sum * 100;
	}

	private float caseFullHouse4(int unknown, int[] ubCN) {
		// denominator - > 분모 / molecule -> 분자 / ubCN ->사용가능한 카드 배열

		float sum = 0f, molec = 0;

		for(int q = 1 ; q < numberDeck.length ; q++)
		{
			if(numberDeck[q] == 0)
			{
				molec = ubCN[q - 1];
				switch(unknown) {
				case 2:
					sum += formulaA(molec, denom);
					break;
				case 3:
					sum += (formulaA(molec, denom) * (exceptionCard(ubCN) - molec) / (denom - 2)) * 3;
					break;
				}
			}
		}

		for(int q = 0 ; q < reqSize ;  q++)
			if(numberDeck[requestDeck.get(q).getCNum() - 1] == 1)
			{
				molec = ubCN[requestDeck.get(q).getCNum() - 2];
				switch(unknown) {
				case 2:
					sum += formulaC(molec, exceptionCard(ubCN), denom) * 2;
					break;
				case 3:
					sum += (molec / denom * formulaA(exceptionCard(ubCN), denom - 1)) * 3;
					break;
				}
			}

		return sum * 100;
	}

	private float caseFourCard1(int unknown, int[] ubCN) {
		// denominator - > 분모 / molecule -> 분자 / ubCN ->사용가능한 카드 배열

		float sum = 0f, molec = 0;

		if(unknown == 3)
			for(int q = 0 ; q < reqSize ; q++)
			{
				molec = ubCN[requestDeck.get(q).getCNum() - 2];
				sum += formulaA(molec, denom) * (molec - 2) / (denom - 2);
			}

		return sum * 100;
	}

	private float caseFourCard2(int unknown, int[] ubCN){
		// denominator - > 분모 / molecule -> 분자 / ubCN ->사용가능한 카드 배열

		float sum = 0f, molec = 0, molec2 = 0;

		for(int q = 0 ; q < reqSize ; q++)
			if(numberDeck[requestDeck.get(q).getCNum() - 1] == 2)
			{
				molec = ubCN[requestDeck.get(q).getCNum() - 2];
				break;
			}

		switch(unknown) {
		case 2:
			sum += formulaA(molec, denom);
			break;
		case 3:
			for(int q = 0 ; q < reqSize ; q++)
				if(numberDeck[requestDeck.get(q).getCNum() - 1] == 1)
				{
					molec2 = ubCN[requestDeck.get(q).getCNum() - 2];
					sum += formulaA(molec2, denom) * (molec2 - 2) / (denom - 2);
				}
			sum += formulaA(molec, denom);
			break;
		}
		return sum * 100;
	}

	private float caseFourCard3(int[] ubCN){
		// denominator - > 분모 / molecule -> 분자 / ubCN ->사용가능한 카드 배열

		ArrayList<Integer> doubleList = getDoubleList();
		float sum = 0f;

		for(int q = 0 ; q < doubleList.size() ; q++)
			sum += formulaA(ubCN[doubleList.get(q) - 2], denom);

		return sum * 100;
	}

	private float caseFourCard4(int unknown, int[] ubCN){
		// denominator - > 분모 / molecule -> 분자 / ubCN ->사용가능한 카드 배열

		float sum = 0f, molec = 0f;
		for(int q = 1 ; q < numberDeck.length ; q++)
		{
			if(numberDeck[q] == 3)
			{
				molec = ubCN[q - 1];
				sum += molec / denom ;
			}
			else if(unknown == 3 && numberDeck[q] == 1)
			{
				molec = ubCN[q - 1];
				sum += formulaA(molec, denom) * (molec - 2) / (denom - 2);
			}
		}
		return sum * 100;
	}

	private float caseStraightFlush(int unknown, int[] ubCN) {
		// denominator - > 분모 / molecule -> 분자 / ubCN ->사용가능한 카드 배열
		
		ArrayList<int[][]> stfGroup = getStfGroup();
		ArrayList<String> needList = new ArrayList<String>();
		float sum = 0f;

		System.out.println("<가능한 스트레이트 플러쉬>──<필요한 모양(-1 ~ -4)>");
		for(int q = 0 ; q < stfGroup.size() ; q++)
			System.out.println(Arrays.deepToString(stfGroup.get(q)));
		
		for(int q = 0 ; q < stfGroup.size() ; q++) {
			needList.clear();
			int count = 0, except = 0, shape = 0, cardNum = 0;
			float result = 0;
			
			for(int w = 0 ; w < stfGroup.get(q)[1].length ; w++) {
				if(stfGroup.get(q)[1][w] == 1) {
					count ++;
				}
				else if(stfGroup.get(q)[1][w] < 0) {
					
					shape = -stfGroup.get(q)[1][w];
					cardNum = stfGroup.get(q)[0][w];
					needList.add("Card" + cardNum + shape);

					if(except == 0)
						except = getExcept(stfGroup.get(q), ubCN, -stfGroup.get(q)[1][w]);
				}
			}

			switch(count) {
			case 2:
				if(unknown == 3)
					result = (formulaC(checkMemory(needList.get(0)), checkMemory(needList.get(1)), denom) * checkMemory(needList.get(2)) / (denom - 2)) * 6;
				break;
			case 3:
				switch(unknown) {
				case 2:
					result = (formulaC(checkMemory(needList.get(0)), checkMemory(needList.get(1)), denom)) * 2;
					break;
				case 3:
					result = (formulaC(checkMemory(needList.get(0)), checkMemory(needList.get(1)), denom) * ((float)(denom - 2) - except) / (denom - 2)) * 6;
					break;
				}
				break;
			case 4:
				switch(unknown) {
				case 2:
					result = (checkMemory(needList.get(0)) / denom * ((float)(denom - 1) - except) / (denom - 1)) * 2;
					break;
				case 3:
					result = (checkMemory(needList.get(0)) / denom * formulaA(((denom - 1) - except), denom - 1)) * 3;
					break;
				}
				break;
			}
			if(stfGroup.get(q)[0][0] != 10)
				sum += result;
			else
				predictArr[8] = result * 100;
		}
		return sum * 100;
	}

	private ArrayList<Integer> getDoubleList() {
		// 2개이상 보유하고있는 카드의 모음
		ArrayList<Integer> tmpList = new ArrayList<Integer>();
		
		for(int q = 0 ; q < reqSize ; q++)
			if(numberDeck[requestDeck.get(q).getCNum() - 1] == 2) {
				
				int tmp = 0;
				for(int w = 0 ; w < tmpList.size() ; w++) {
					if(requestDeck.get(q).getCNum() == tmpList.get(w))
						tmp ++;
				}
				if(tmp == 0)
					tmpList.add(requestDeck.get(q).getCNum());
			}
		return tmpList;
	}

	private int getExcept(int[][] EnableStraight, int[] usableCardNum, int shape) {
		// 스트레이트 구성시 제외해야할 카드의 남은 카드수를 except에 저장
		int except = 0;
		int beginNum = EnableStraight[0][0];
		int endNum = EnableStraight[0][4];

		if(shape == 0) {
			if(endNum != 14)
				except += usableCardNum[endNum - 1];

			if(beginNum != 2 && beginNum != 14)
				except += usableCardNum[beginNum - 3];

			else if(beginNum == 2)
				except += usableCardNum[12];
		} else {
			if(endNum != 14)
				except += checkMemory("Card" + (endNum + 1) + shape);

			if(beginNum != 2 && beginNum != 14)
				except += checkMemory("Card" + (endNum - 1) + shape);

			else if(beginNum == 2)
				except += checkMemory("Card" + 14 + shape);
		}
		return except;
	}
	
	private float checkMemory(String Key){//AI의 메모리에 조회한 카드가 있나 없나 체크
		for(int q = 0 ; q < user.getCardMemory().size() ; q++)
			if(Key.equals(user.getCardMemory().get(q).getPrimaryKey()))
				return 0f;
		
		return 1f;
	}

	private ArrayList<int[][]> getStGroup(){
		ArrayList<int[][]> tmpList = new ArrayList<int[][]>();

		for(int q = 0 ; q < 10 ; q++)
		{
			int[][] straightArr = new int[2][5];
			int straightCheck = 0;
			int tmp = 0;

			for(int w = q ; w < q + 5 ; w++)
			{
				straightArr[0][tmp] = w == 0 ? w + 14 : w + 1;
				if(numberDeck[w] >= 1)
				{
					straightArr[1][tmp] = 1;
					straightCheck ++;
				}

				tmp ++;
			}
			if(straightCheck >= 2)
				tmpList.add(straightArr);
		}
		return tmpList;
	}

	private ArrayList<int[][]> getStfGroup() {
		ArrayList<int[][]> tmpList = new ArrayList<int[][]>();
		ArrayList<Integer> shapeCandidate = new ArrayList<Integer>();

		for(int q = 0 ; q < shapeDeck.length ; q++)
			if(shapeDeck[q] >= 2)
				shapeCandidate.add(q + 1);

		for(int q = 0 ; q < shapeCandidate.size() ; q++)
			for(int w = 0 ; w < 10 ; w++)
			{
				int[][] straightArr = new int[2][5];
				int straightFlushCheck = 0;
				int tmp = 0;

				for(int e = w ; e < w + 5 ; e++)
				{
					straightArr[0][tmp] = e == 0 ? e + 14 : e + 1;
					for(int r = 0 ; r < requestDeck.size() ; r++)
						if(requestDeck.get(r).getCNum() == straightArr[0][tmp] 
						&& requestDeck.get(r).getCShape() == shapeCandidate.get(q))
						{
							straightArr[1][tmp] = 1;
							straightFlushCheck ++;
						}
					
					if(straightArr[1][tmp] == 0)
						straightArr[1][tmp] = -shapeCandidate.get(q);
					
					tmp ++;
				}
				if(straightFlushCheck >= 2)
					tmpList.add(straightArr);
			}
		return tmpList;
	}

	@Override
	public void initA(){
		denom = 52;
		Arrays.fill(usableCardNum, 4);
		Arrays.fill(usableCardShape, 13);
		Arrays.fill(predictArr, 0);
	}
}