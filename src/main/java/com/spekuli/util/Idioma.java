package com.spekuli.util;

public enum Idioma {
	PORTUGUES {
		@Override
		public String toString() {return "Portugu�s";}
		@Override
		public String codigo() {return "pt";}
	},
	ESPANHOL {
		@Override
		public String toString() {return "Espa�ol";}
		@Override
		public String codigo() {return "es";}
	},
	INGLES {
		@Override
		public String toString() {return "English";}
		@Override
		public String codigo() {return "en";}
	};
	
	@Override
	public String toString() {return null;}
	public String codigo() {return null;}
}
