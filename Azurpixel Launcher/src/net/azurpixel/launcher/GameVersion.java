package net.azurpixel.launcher;

public enum GameVersion {
	
		V1_8("1.8", "assets/indexes/AzurPixel_1.8.json"),
		V1_9("1.9", "assets/indexes/AzurPixel_1.9.json"),
		V1_10("1.10", "assets/indexes/AzurPixel_1.10.json"),
		V1_11("1.11", "assets/indexes/AzurPixel_1.11.json"),
		V1_12("1.12", "assets/indexes/AzurPixel_1.12.json"),
		V1_13("1.13", "assets/indexes/AzurPixel_1.13.json"),
		V1_14("1.14", "assets/indexes/AzurPixel_1.14.json");

		private String name;

		GameVersion(String name, String Folder) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}

}