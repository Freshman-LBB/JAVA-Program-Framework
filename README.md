# JAVA-Program-Framework  

## Project Overview  

A Java-based game framework for an Airplane Battle mini-game, utilizing a fully object-oriented approach with a circular doubly linked list and free nodes to efficiently manage game entities.  

## Key Features  

- 🚀 **Object-Oriented Design**: Multi-level inheritance (one-to-one, one-to-many) with clear class boundaries  
- 🔗 **Circular Doubly Linked List**: Comprehensive restructuring of traditional procedural development  
- 🌐 **High Extensibility**: Supports complex inheritance relationships and modular programming  
- ⚡ **Performance Optimization**: Reduced resource reloading, smooth execution  

## Framework Structure  

### Core Classes  

1. **Node Class**: Base entity management  
   - Manages game entity states  
   - Provides key methods:  
     - `forward()`: Entity update logic  
     - `activate()`: Entity activation control  
     - `checkCollision()`: Collision detection  

2. **NodeList Class**: Node list management  
   - Implements circular node movement  
   - Provides comprehensive collision detection  

3. **CircularDoubleLinkedListWithFreeNode Class**: Resource core  
   - Manages active and idle entity resources  
   - Dynamic node allocation  
   - Optimizes memory usage and processing efficiency  

### Entity Types  

- **Enemy**: Abstract base enemy class  
- **CommonEnemy**: Concrete enemy implementation  
- **Hero**: Player character  
- **Weapon**: Weapon management system  

## Quick Start  

### Clone the Project  

```bash  
git clone https://github.com/Freshman-LBB/JAVA-Program-Framework.git  
Build and Run
bash
# Compile project  
mvn clean install  

# Run game  
java -jar target/airplane-battle.jar  
Extension Example
To add a new enemy type:

Inherit from Enemy base class
Implement/Override:
Copy constructor
forward() method (movement logic)
activate() method (spawn mechanism)
Define specific attributes and resources
Project Advantages
🚀 Maximized code reusability
🔧 Flexible and modular design
⚡ Efficient performance optimization
🌐 Adaptable to various game development scenarios
Future Roadmap
Event encapsulation
Unified resource management
Enhanced entity management
Contributing
Fork the project
Create feature branch (git checkout -b feature/AmazingFeature)
Commit changes (git commit -m 'Add some AmazingFeature')
Push to branch (git push origin feature/AmazingFeature)
Submit Pull Request
License
MIT License

Development Team
Li Binbin: Framework design, core implementation
Liao Zhanhong: UML design, auxiliary development
