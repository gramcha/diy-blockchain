/**
 * @author gramcha
 * 22-Dec-2017 6:02:48 PM
 * 
 */
package com.gramcha.entities;

import java.util.List;

public class BlockChainList {
	List<Block> data;

	public List<Block> getData() {
		return data;
	}

	public void setData(List<Block> data) {
		this.data = data;
	}

	public void addBlockToList(Block item) {
		data.add(item);
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BlockChainList [data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}

}
