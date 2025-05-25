import os
import json

def generate_cars_json(dataset_path):
    cars_data = []
    
    # Walk through the dataset directory
    for root, dirs, files in os.walk(dataset_path):
        # Get the brand name from the directory name (remove the number prefix)
        if dirs:
            for dir_name in dirs:
                brand = dir_name.split('-', 1)[1] if '-' in dir_name else dir_name
                brand_path = os.path.join(root, dir_name)
                
                # Get all PNG files for this brand
                png_files = [f for f in os.listdir(brand_path) if f.endswith('.png')]
                
                # Create an entry for each image
                for png_file in png_files:
                    # Get the model number from the filename
                    model = png_file.split('-')[-1].replace('.png', '')
                    
                    # Create the relative path for the image
                    image_path = os.path.join('data', 'Car_Logo_Dataset', dir_name, png_file)
                    
                    car_data = {
                        "brand": brand,
                        "model": f"Logo-{model}",
                        "imageUrl": f"https://raw.githubusercontent.com/YOUR_USERNAME/guess_the_car/main/{image_path}"
                    }
                    cars_data.append(car_data)
    
    # Write to JSON file in the data directory
    os.makedirs('data', exist_ok=True)
    with open('data/cars.json', 'w') as f:
        json.dump(cars_data, f, indent=2)

if __name__ == "__main__":
    # Use the data directory in the project
    dataset_path = "data/Car_Logo_Dataset"
    generate_cars_json(dataset_path) 