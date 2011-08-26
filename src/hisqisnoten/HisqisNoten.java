/* Copyright (C) 2010-2011 Christoph Giesel
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package hisqisnoten;

/**
 * 
 * @author Christoph Giesel
 *
 */
public class HisqisNoten {
    
    public static void main(String[] args) {
        String user = null;
        String pass = null;

        if (args.length == 2) {
            user = args[0];
            pass = args[1];
        }

        new HisqisConsole(user, pass);
    }
}
