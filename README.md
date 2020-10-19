# TrollDox
TrollDox parses a proprietary scripting language without a name (that I call
Troll) to create flow-charts for the script which can then be rendered by the
GraphViz layout engine. While I wouldn't have described it this way at the time,
this project was actually my first compiler.

### Examples
1. The statement : `LOGEVENT "Hello World!"` is rendered as:
> Logevent: Hello World!

2. Troll has long, redundant, global variable names. TrollDox simplifies them
   for readability. So the name `Application:Project_Name.Console_Parameter:Console_Parameters.AO:_01234_Parameter_Name_Here.Value
   1` is rendered as: `Param:AO Parameter Name Here`
