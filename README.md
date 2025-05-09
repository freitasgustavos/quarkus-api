## 🚀 Executando o projeto

Iniciar o continer do banco de dados

```shell script
docker compose up -d
```

Executar o projeto

```shell script
./mvnw quarkus:dev
```
Aplicação disponível na URL `http://localhost:8080`

## Listagem API REST

- [Api.yaml](https://github.com/freitasgustavos/quarkus-api/blob/main/api.yaml)

## Documentação Swagger

Disponível na URL `http://localhost:8080/swagger-ui/`

## Diagrama de classes

- https://www.planttext.com/

```
@startuml

class Budget {
    Long id
    LocalDate creationDate
    LocalDate validityDate
    BigDecimal grossTotalAmount
    BigDecimal totalDiscountAmount
    BigDecimal netTotalAmount
    +recalculateTotals()
}

class BudgetItem {
    Long id
    int quantity
    BigDecimal grossUnitPrice
    BigDecimal itemDiscountAmount
    BigDecimal netUnitPrice
    BigDecimal netTotalItemAmount
    +calculateValues()
    +setBudget(Budget)
}

class Person {
    Long id
    String name
    String documentNumber
    LocalDate birthDate
    String phone
    String email
}

class HealthPlan {
    Long id
    String name
    String ansRegistry
}

abstract class BudgetTableItem {
    Long id
    String name
    BigDecimal basePrice
    +getItemType()
}

class Exam {
    String tussCode
    String preparationInstructions
}

class Fee {
    String description
}

Budget "1" o-- "*" BudgetItem : items
Budget "*" o-- "1" Person : person
Budget "*" o-- "1" HealthPlan : healthPlan
BudgetItem "*" o-- "1" BudgetTableItem : item
BudgetTableItem <|-- Exam
BudgetTableItem <|-- Fee

@enduml
```
