# vending_machine.py

# ================== OBSERVER ==================
class Observer:
    def update(self, data):
        pass


class MoneyObserver(Observer):
    def update(self, amount):
        print(f"[Observer] Suma curenta: {amount} lei")


class ProductObserver(Observer):
    def __init__(self, vending_machine):
        self.vending_machine = vending_machine

    def update(self, product):
        print(f"[Observer] Produs selectat: {product}")
        self.vending_machine.process_order(product)


# ================== TAKE MONEY STM ==================
class TakeMoneySTM:
    def __init__(self):
        self.amount = 0
        self.observers = []

    def add_observer(self, obs):
        self.observers.append(obs)

    def notify(self):
        for obs in self.observers:
            obs.update(self.amount)

    def insert_money(self, value):
        self.amount += value
        print(f"Ai introdus {value} lei")
        self.notify()

    def reset(self):
        self.amount = 0


# ================== SELECT PRODUCT STM ==================
class SelectProductSTM:
    def __init__(self):
        self.observers = []

    def add_observer(self, obs):
        self.observers.append(obs)

    def notify(self, product):
        for obs in self.observers:
            obs.update(product)

    def select_product(self, product):
        print(f"Ai selectat: {product}")
        self.notify(product)


# ================== VENDING MACHINE ==================
class VendingMachineSTM:
    def __init__(self):
        self.products = {
            "cola": 5,
            "fanta": 4,
            "apa": 3
        }

        self.money_stm = TakeMoneySTM()
        self.product_stm = SelectProductSTM()

        # adăugăm observatori
        self.money_stm.add_observer(MoneyObserver())
        self.product_stm.add_observer(ProductObserver(self))

    def process_order(self, product):
        if product not in self.products:
            print("Produs inexistent")
            return

        price = self.products[product]
        amount = self.money_stm.amount

        if amount >= price:
            print(f"Ai primit {product}")
            change = amount - price

            if change > 0:
                print(f"Rest: {change} lei")

            self.money_stm.reset()
        else:
            print(f"Fonduri insuficiente. Mai ai nevoie de {price - amount} lei")


# ================== MAIN ==================
if __name__ == "__main__":
    vm = VendingMachineSTM()

    # test 1
    vm.money_stm.insert_money(2)
    vm.money_stm.insert_money(2)
    vm.product_stm.select_product("cola")

    print("\n---\n")

    # test 2
    vm.money_stm.insert_money(5)
    vm.product_stm.select_product("fanta")