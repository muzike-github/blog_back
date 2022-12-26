package com.muzike;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class test {
	public static void main(String[] args) {
		String[] a= new String[]{"1","2"};
		int[] b=new int[]{1,22};
		List<String> lists = Arrays.asList(a);
		List<Integer> lis = lists.stream()
				.map(Integer::parseInt).collect(Collectors.toList());
		for (int temp : lis) {
			System.out.println(temp);
		}
	}
}
