Lib--ColorBars
==============

Simple to use library for animating color change for ActionBar, NavigationBar, and status bar


Usage Example:
==============

    public ColorBars(Activity activity, View view, int PaddingBotom,int PaddingTop) {


		mColorBars = new ColorBars(getActivity(), findViewById(R.id.BaseContainer),0,0);
		mColorBars.changeColor(new Color().Red); // Call this at any moment. 
		
		
		
	
	
	Credits
==============

Author: Adam Fręśko
<br>

Based upon: https://github.com/jgilfelt/SystemBarTint


License
==============

Copyright 2013 readyState Software Limited

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
