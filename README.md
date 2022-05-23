# PlantUML Previewer -- A image viewer for PlantUML file

The purpose of this project is to provide a simple way to display images in
Java (using Swing) to aid in preview PlantUML file.

## Downloads

Please refer to the
[Releases](https://github.com/binhtran432k/plantuml-previewer/releases)
page.

## Instruction

To run this file, dowload
[JDK](https://www.oracle.com/java/technologies/downloads/)
in your pc computer. After that you can open
the downloader files (plantuml-previewer\*.jar).

There are two version of plantuml previewer:

- plantuml-previewer*-noplantuml.jar: This version is not include
  plantuml jar inside, and you need to download a `plantuml*.jar`from [PlantUML Releases](https://github.com/plantuml/plantuml/releases). After downloaded, rename it to`plantuml.jar` and place in same directory
  with this version to make it working.
- plantuml-previewer\*.jar: This version is just included plantuml jar
  inside, but the version of plantuml cannot be changed.

## Features

- Automatic reload file when modified (e.g. from editor)
- Support menu bar
- Support drag image
- Support zooming image
- Support multiple image with `newpage`
- Support multiple image with `@startuml ... @enduml`
  in one file (e.g. markdown)

## Keyboard support

- `o`: Open file for viewer to preview
- `q`: Quit viewer
- `r`: Reload Image
- `m`: Toggle Menu
- `c`: Toggle Scroll
- `plus`: Zoom in
- `minus`: Zoom out
- `equal`: Zoom to image size
- `a`: Zoom to best fit the viewer size
- `s`: Zoom to width fit the viewer size
- `n`: Go to next image in file
- `p`: Go to previous image in file
- `h/j/k/l`: Move the image to left/down/up/right
- `shift + h/j/k/l`: Move the image to left/down/up/right x5 speed
- `←/↓/↑/→`: Similar to `h/j/k/l`
- `shift + ←/↓/↑/→`: Similar to `shift + h/j/k/l`
- `0`: Move image to begin (leftmost)
- `shift + 4`: Move image to end (rightmost)
- `g`: Move image to top
- `shift + g`: Move image to bottom

## Mouse support

- `mouse click -> move mouse -> release click`: Drag -> Move -> Drop image
- `mouse wheel up`: Move image up
- `mouse wheel down`: Move image down
- `shift + mouse wheel up`: Move image left
- `shift + mouse wheel down`: Move image right
- `ctrl + mouse wheel up`: Zoom in
- `ctrl + mouse wheel down`: Zoom out

**Note**: The wheel up and down may be reversed base on system.

## Demo

- Open file + show menu + navigate image:
  ![ewrewrew](./media/open_menu_navigate.gif)

- Auto reload image + zooming:
  ![ewrewrew](./media/autoreload_zooming.gif)

## TODO

- [x] Main panel
- [x] Status panel
- [x] Menu bar
- [x] PlantUML image viewer
- [x] Image zoom
- [x] Image manipulation
- [x] Keybindings
- [x] Keybindings Help
- [x] Open File Feature
- [x] Documentation
- [ ] About
- [ ] Export File Feature
- [ ] PlantUML server support
- [ ] External PlantUML source support
- [ ] User Preferences support
- [ ] Colorschemes support
- [ ] Svg Preview support
