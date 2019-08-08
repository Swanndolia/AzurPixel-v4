package net.azurpixel.launcher;


public enum GamePreset {
	
		Mini("mini", "presets/mini/optionsof.txt"),
		Low("low", "presets/low/optionsof.txt"),
		Medium("medium", "presets/medium/optionsof.txt"),
		High("high", "presets/high/optionsof.txt"),
		Ultra("ultra", "presets/ultra/optionsof.txt"),
		Custom("custom", "presets/custom/optionsof.txt");

		private String name;

		GamePreset(String name, String Folder) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}

}